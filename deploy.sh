#!/bin/bash

# Exit immediately if a command exits with a non-zero status
set -e

# Define variables
PROJECT_ID="telusrecruitai-468907"
IMAGE_NAME="recruitai-authentication"
REGION="asia-south1"  # Change this to your preferred region
SERVICE_NAME="recruitai-authentication"

# Build the Docker image
docker build -t $IMAGE_NAME .

# Tag the image for Google Container Registry
docker tag $IMAGE_NAME gcr.io/$PROJECT_ID/$IMAGE_NAME

# Push the image to Google Container Registry
docker push gcr.io/$PROJECT_ID/$IMAGE_NAME

# Deploy to Cloud Run
gcloud run deploy $SERVICE_NAME \
  --image gcr.io/$PROJECT_ID/$IMAGE_NAME \
  --platform managed \
  --region $REGION \
  --allow-unauthenticated
