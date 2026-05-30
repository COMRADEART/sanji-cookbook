# Scripts Directory

This directory contains scripts and backend services that support the Sanji Cookbook application.

## Backend Services (`/chef_backend`)

The `chef_backend` directory contains a Python-based FastAPI service that provides the AI capabilities for the Chef Companion.

### Purpose
- **AI Brain**: Orchestrates responses using Sanji's persona.
- **Vision**: Recognizes food ingredients from images using Gemini/Vision models.
- **Meal Planning**: Generates context-aware meal plans.
- **Recipe Generation**: Creates professional recipes based on identified ingredients.

### Tech Stack
- **FastAPI**: Web framework.
- **Whisper**: For audio transcription.
- **Google Generative AI**: For the core "Chef" logic.
- **Redis**: For caching and session management.

### Build & Integration
- The backend is containerized using the provided `Dockerfile`.
- It is deployed to Google Cloud Run (as indicated by `cloudbuild.yaml`).
- The Android app communicates with this service via the `SanjiChefApi` defined in the `presentation` module.

## How to Run Locally

1. Navigate to `chef_backend`.
2. Install dependencies: `pip install -r requirements.txt`.
3. Run the service: `uvicorn app.main:app --reload`.

Ensure you have the necessary environment variables configured in a `.env` file (see `chef_backend/app/main.py` for required keys).
