# TrailPoint (Android)

This package is the Android coding exercise starter: app shell, unit tests, and a
local mock HTTP server.

## Project Structure

```
zoleo-trailpoint-android-interview-coding-challenge/
├── README.md          # Package overview
├── TrailPoint/        # Jetpack Compose app — see TrailPoint/README.md
└── mock-server/       # Local API — see mock-server/README.md
```

- App setup: [TrailPoint/README.md](TrailPoint/README.md)
- Mock server and API: [mock-server/README.md](mock-server/README.md)

## Quick Start

```bash
cd mock-server && npm ci && npm start
```

In another terminal: `curl http://localhost:4747/devices`

Then build and test the app per [TrailPoint/README.md](TrailPoint/README.md).
The emulator uses `http://10.0.2.2:4747` (see mock-server Base URLs).

## Scope

Editable: Kotlin source, tests, and resources under `TrailPoint/`.

Out of scope: Any changes to `mock-server/`, release/CI/distribution tasks, or work unrelated to the Android mobile app.

## Tech Stack

- **Android:** Kotlin, Jetpack Compose
- **Testing:** JUnit
- **Mock server:** Node.js, Express, TypeScript
