# Ubiquiti Device Management API, interview task 🚀

This is a Spring Boot application implementing DeviceAPI for the Ubiquiti interview. It provides RESTful API endpoints to register devices, retrieve device information, and get device topology.

## Notes

- For storing network devices, simple HashMap was used instead of a database due to simplicity. Check DeviceService class.
- To find a topology starting with a specific device, a simple recursive function was implemented to traverse the network devices and find the subtree.
- The application is containerized using Docker and Docker Compose.

## Prerequisites 📋

- Docker 🐳
- Docker Compose 🛠️

## Quick Start Guide 🏃‍♂️

1. **Clone the repository**:
    ```sh
    git clone https://github.com/knedlicc/ubiquiti-interview.git
    cd ubiquiti-interview
    ```

2. **Build and run the application using Docker Compose**:
    ```sh
    docker-compose up --build
    ```

3. **Access the API**:
   The application will be running at `http://localhost:8080`.

## Starting the Application without Docker 🖥️

If you prefer to run the application without Docker, follow these steps:

1. **Ensure you have Java and Maven installed**.

2. **Build the project**:
    ```sh
    mvn clean install
    ```

3. **Run the application**:
    ```sh
    mvn spring-boot:run
    ```

The application will be running at `http://localhost:8080`.

## API Endpoints 📡

### Register a Device 📝

- **URL**: `/api/devices`
- **Method**: `POST`
- **Request Body**:
    ```json
    {
        "deviceType": "GATEWAY",
        "macAddress": "00:11:22:33:44:55",
        "uplinkMacAddress": "66:77:88:99:AA:BB"
    }
    ```
- **Response**: `200 OK`

### Get All Devices 📋

- **URL**: `/api/devices`
- **Method**: `GET`
- **Response**:
    ```json
    [
        {
            "deviceType": "GATEWAY",
            "macAddress": "00:11:22:33:44:55",
            "uplinkMacAddress": null
        },
        {
            "deviceType": "SWITCH",
            "macAddress": "66:77:88:99:AA:BB",
            "uplinkMacAddress": null
        }
    ]
    ```

### Get Device by MAC Address 🔍

- **URL**: `/api/devices/{macAddress}`
- **Method**: `GET`
- **Response**:
    ```json
    {
        "deviceType": "GATEWAY",
        "macAddress": "00:11:22:33:44:55",
        "uplinkMacAddress": null
    }
    ```

### Get Device Topology 🌐

- **URL**: `/api/topology`
- **Method**: `GET`
- **Response**:
    ```json
    {
        "00:11:22:33:44:55": ["66:77:88:99:AA:BB"]
    }
    ```

### Get Device Topology from a MAC Address 🌐

- **URL**: `/api/topology/{macAddress}`
- **Method**: `GET`
- **Response**:
    ```json
    {
        "00:11:22:33:44:55": ["66:77:88:99:AA:BB"],
        "66:77:88:99:AA:BB": ["CC:DD:EE:FF:00:11"]
    }
    ```
