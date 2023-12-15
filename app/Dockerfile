FROM node:16.11.1-alpine
WORKDIR /app
COPY package.json .
ENV REACT_APP_GATEWAY 211.62.99.58:7780
RUN npm install --force
COPY . .
EXPOSE 3000
CMD ["npm", "start"]