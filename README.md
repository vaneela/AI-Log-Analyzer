# AI-Driven Log & Error Analyzer

A Spring Boot backend service that ingests application logs and uses an LLM (OpenAI `gpt-4o-mini`) to generate a structured analysis:
- High-level summary of issues
- Key error patterns
- Possible root causes
- Suggested next actions

## Tech Stack

- Java 17
- Spring Boot 3
- Spring Web & WebClient
- JSON / Jackson
- OpenAI Chat Completions API (`gpt-4o-mini`)

## How It Works

1. Client sends `environment` and raw `logs` text to the API.
2. Service pre-processes logs (filters `ERROR`/`WARN` lines).
3. Builds a structured prompt and calls the OpenAI API.
4. Parses the LLM response into a JSON object:

```json
{
  "summary": "...",
  "keyErrors": ["..."],
  "possibleCauses": ["..."],
  "suggestedActions": ["..."]
}
