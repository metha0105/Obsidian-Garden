FROM alpine:latest

# Install build tools and Python (no trailing slashes!)
RUN apk add --no-cache \
    g++ \
    gcc \
    make \
    libc-dev \
    python3 \
    py3-pip \
    cmake \
    git

# Set working directory inside container
WORKDIR /app

# Copy all files into the /app directory inside the container
COPY . .

# Compile the C++ evaluator
RUN g++ -std=c++17 -O2 -o evaluator main.cpp

# Set default command when container runs
CMD ["./evaluator"]