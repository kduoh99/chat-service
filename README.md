# Chat Service

실시간 채팅, Redis Pub/Sub, SSE, ELK 로그 모니터링을 포함한 백엔드 중심의 학습용 프로젝트입니다.


## 📌 프로젝트 개요

> WebSocket/STOMP 기반의 1:1 및 그룹 채팅 서비스입니다.  
Redis Pub/Sub을 적용해 다중 서버 환경에서도 메시지를 일관되게 브로드캐스트하고, 읽지 않은 메시지 수는 SSE(Server-Sent Events)를 통해 실시간으로 반영합니다.  
로그는 Logback → Logstash → Elasticsearch로 수집되며, Kibana에서 시각화되고 Slack Webhook을 통해 알림이 전송됩니다.


## 🛠️ Tech Stack

### Backend  
![JDK](https://img.shields.io/badge/JDK-21-007396?style=flat&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-6DB33F?style=flat&logo=springboot&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-%20-6DB33F?style=flat&logo=spring&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-%20-6DB33F?style=flat&logo=springsecurity&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-%20-4479A1?style=flat&logo=mysql&logoColor=white)
![Spring Data Redis](https://img.shields.io/badge/Spring%20Data%20Redis-%20-DC382D?style=flat&logo=redis&logoColor=white)
![WebSocket](https://img.shields.io/badge/WebSocket-%20-333333?style=flat)
![Lombok](https://img.shields.io/badge/Lombok-%20-CA0C00?style=flat&logo=lombok&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-%20-000000?style=flat&logo=jsonwebtokens&logoColor=white)

### Frontend  
![Node.js](https://img.shields.io/badge/Node.js-20.14.0-339933?style=flat&logo=node.js&logoColor=white)
![Vue.js](https://img.shields.io/badge/Vue.js-3-4FC08D?style=flat&logo=vue.js&logoColor=white)
![Vue Router](https://img.shields.io/badge/Vue%20Router-%20-4FC08D?style=flat&logo=vue.js&logoColor=white)
![Vuetify](https://img.shields.io/badge/Vuetify-3-1867C0?style=flat&logo=vuetify&logoColor=white)
![Axios](https://img.shields.io/badge/Axios-%20-5A29E4?style=flat)
![SockJS Client](https://img.shields.io/badge/SockJS%20Client-%20-CC0000?style=flat)
![WebStomp Client](https://img.shields.io/badge/WebStomp%20Client-%20-6A1B9A?style=flat)
![jwt-decode](https://img.shields.io/badge/jwt--decode-%20-000000?style=flat)
![event-source-polyfill](https://img.shields.io/badge/event--source--polyfill-%20-FFC107?style=flat)

### Infra  
![Docker](https://img.shields.io/badge/Docker-%20-2496ED?style=flat&logo=docker&logoColor=white)
![Nginx](https://img.shields.io/badge/Nginx-%20-009639?style=flat&logo=nginx&logoColor=white)
![Amazon EC2](https://img.shields.io/badge/AWS%20EC2-%20-FF9900?style=flat)
![Amazon RDS](https://img.shields.io/badge/AWS%20RDS-%20-527FFF?style=flat&logo=amazonrds&logoColor=white)
![Amazon ElastiCache](https://img.shields.io/badge/ElastiCache-%20-2D72D9?style=flat)
![Amazon ECR](https://img.shields.io/badge/ECR-%20-FF9900?style=flat)
![Amazon Route 53](https://img.shields.io/badge/Route%2053-%20-8C4FFF?style=flat)
![Elasticsearch](https://img.shields.io/badge/Elasticsearch-8.x-005571?style=flat&logo=elasticsearch&logoColor=white)
![Logstash](https://img.shields.io/badge/Logstash-8.x-000000?style=flat&logo=logstash&logoColor=white)
![Kibana](https://img.shields.io/badge/Kibana-8.x-E8478B?style=flat&logo=kibana&logoColor=white)
![Slack](https://img.shields.io/badge/Slack-Webhook-4A154B?style=flat&logo=slack&logoColor=white)


## 🏗️ Architecture

![Architecture Diagram](https://github.com/user-attachments/assets/8b47486c-d003-4a8a-9e2c-59b83d92162b)


## 📈 Monitoring

<img width="1348" alt="Kibana Dashboard" src="https://github.com/user-attachments/assets/c1d1f55e-6522-4cc8-b6df-f57ccbd1bb81" />  
<img width="1104" alt="Slack Alert" src="https://github.com/user-attachments/assets/06eb79c9-7506-4f40-8267-4f5673f02012" />
