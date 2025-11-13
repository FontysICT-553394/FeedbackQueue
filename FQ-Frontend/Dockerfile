# -- Build --
FROM node:20-alpine AS builder
WORKDIR /app

# Install dependencies
COPY package*.json ./
RUN npm ci

# Copy source and build
COPY . .
RUN npm run build

# Removes unused packages
RUN npm prune --omit=dev

# -- Runtime --
FROM node:20-alpine AS runner
WORKDIR /app

ENV NODE_ENV=production

# Bring over build output and production dependencies
COPY --from=builder /app/.output ./.output
COPY --from=builder /app/node_modules ./node_modules
COPY --from=builder /app/package*.json ./

EXPOSE 3000
CMD ["node", ".output/server/index.mjs"]
