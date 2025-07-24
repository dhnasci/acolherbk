FROM gradle:6.8.3-jdk11 AS build

WORKDIR /app

COPY . /app
RUN gradle build --no-daemon

EXPOSE 8080

CMD ["gradle", "bootRun", "--no-daemon"]