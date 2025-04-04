# stage 0: generate mock server code
FROM gcatanese/openapi-native-mock-server

ARG openapifile=openapi/openapi.yaml
ADD $openapifile /openapi/$openapifile

RUN java -cp /openapi/bin/openapi-native-mock-server.jar:/openapi/bin/openapi-generator-cli.jar \
  org.openapitools.codegen.OpenAPIGenerator generate -g com.tweesky.cloudtools.codegen.NativeMockServerCodegen \
   -i /openapi/$openapifile -o /openapi/go-server

# stage 1: build Go executable
FROM golang:1.22-alpine3.19
COPY --from=0 /openapi/go-server ./go-server

WORKDIR /go/go-server
RUN go mod tidy

RUN CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build -o /build .

# stage 2: build minimal image
FROM scratch AS runtime

COPY --from=1 /build .

EXPOSE 8080
ENTRYPOINT ["./build"]

