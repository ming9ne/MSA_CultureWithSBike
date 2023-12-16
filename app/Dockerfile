FROM node:16.11.1-alpine
WORKDIR /app
COPY package.json .
COPY . .
RUN npm install --force
EXPOSE 3000