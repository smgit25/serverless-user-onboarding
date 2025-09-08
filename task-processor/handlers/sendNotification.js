import { SESClient, SendEmailCommand } from "@aws-sdk/client-ses";
import { SSMClient, GetParameterCommand } from "@aws-sdk/client-ssm";

const ssm = new SSMClient({});
const ses = new SESClient({});
const PARAM_EMAIL = process.env.PARAM_EMAIL_FROM || "/cloud-native-api-orchestration-prod/emailFrom";

async function getParam(name) {
  try {
    const res = await ssm.send(new GetParameterCommand({ Name: name }));
    return res.Parameter?.Value;
  } catch (e) {
    console.warn("SSM getParam failed", e.message);
    return null;
  }
}

export const handler = async (event) => {
  console.log("SNS event:", JSON.stringify(event, null, 2));
  const emailFrom = await getParam(PARAM_EMAIL) || "no-reply@example.com";

  for (const record of event.Records || []) {
    const msg = JSON.parse(record.Sns.Message);
    const user = msg.user || msg.detail || {};
    const to = user.email;
    if (!to) {
      console.log("No email present, skip.");
      continue;
    }
    const params = {
      Destination: { ToAddresses: [to] },
      Source: emailFrom,
      Message: {
        Subject: { Data: "Welcome!" },
        Body: { Html: { Data: `<p>Hi ${user.name || 'there'}, welcome!</p>` } }
      }
    };
    try {
      await ses.send(new SendEmailCommand(params));
      console.log("Sent email to", to);
    } catch (err) {
      console.error("SES send error", err);
    }
  }
};
