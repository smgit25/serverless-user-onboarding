import { SQSClient, DeleteMessageCommand } from "@aws-sdk/client-sqs";
import fetch from "node-fetch";
import { DynamoDBClient, UpdateItemCommand } from "@aws-sdk/client-dynamodb";

const sqs = new SQSClient({});
const ddb = new DynamoDBClient({});
const USERS_TABLE = process.env.USERS_TABLE;

export const handler = async (event) => {
  console.log("Received SQS event:", JSON.stringify(event, null, 2));
  for (const record of event.Records || []) {
    try {
      const body = JSON.parse(record.body);
      const user = body.user || body;
      const id = user.id;
      const email = user.email;

      // Example external API enrichment call (replace with real)
      const res = await fetch("https://jsonplaceholder.typicode.com/users/1");
      const ok = res.status === 200;

      // Update DynamoDB: set status
      const status = ok ? "VERIFIED" : "PENDING";
      const updateCmd = new UpdateItemCommand({
        TableName: USERS_TABLE,
        Key: { id: { S: id } },
        UpdateExpression: "SET #s = :s, lastProcessedAt = :ts",
        ExpressionAttributeNames: { "#s": "status" },
        ExpressionAttributeValues: {
          ":s": { S: status },
          ":ts": { S: new Date().toISOString() }
        }
      });
      await ddb.send(updateCmd);

      console.log(`Processed user ${id} -> ${status}`);
      // Optionally publish SNS / send notification (via separate lambda)
    } catch (err) {
      console.error("Error processing record", err);
      throw err; // let Lambda/SQS handle retry/DLQ
    }
  }
};
