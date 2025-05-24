# 💬 Chat Service

> WebSocket/STOMP 기반의 실시간 채팅, SSE 알림, Redis Pub/Sub, ELK 로그 모니터링, 부하 테스트를 포함한 백엔드 중심의 학습용 프로젝트입니다.


## 📌 Overview

WebSocket/STOMP 기반으로 1:1 및 그룹 채팅 기능을 구현하고, Redis Pub/Sub을 적용하여 다중 서버 간 메시지를 안정적으로 브로드캐스트합니다.  
읽지 않은 메시지 수는 SSE(Server-Sent Events)를 통해 실시간으로 반영됩니다.  
로그는 Logback → Logstash → Elasticsearch로 수집되며, Kibana에서 시각화되고 Slack Webhook을 통해 오류 알림이 전송됩니다.  
부하 테스트는 k6를 활용하여 수행하고, k6 Overview와 CloudWatch를 통해 응답 지표·시스템 자원을 모니터링합니다.  
테스트 결과를 기반으로 병목 지점을 진단하고, 스케일 업·스케일 아웃 전략을 적용하여 비교한 뒤 성능을 개선했습니다.

## 🎬 Demo

<img src="https://github.com/kduoh99/chat-service/blob/main/images/demo/chat-demo.gif?raw=true" width="800"/>


## 🏗️ Architecture

<img src="https://github.com/kduoh99/chat-service/blob/main/images/infra/chat-service-architecture.jpg?raw=true" width="800"/>


## 🛠️ Tech Stack

### Backend  
![JDK](https://img.shields.io/badge/JDK-21-007396?style=flat&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-6DB33F?style=flat&logo=springboot&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-%20-6DB33F?style=flat&logo=spring&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-%20-6DB33F?style=flat&logo=springsecurity&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-%20-4479A1?style=flat&logo=mysql&logoColor=white)
![Spring Data Redis](https://img.shields.io/badge/Spring%20Data%20Redis-%20-DC382D?style=flat&logo=redis&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-%20-CA0C00?style=flat&logo=lombok&logoColor=white)
![WebSocket](https://img.shields.io/badge/WebSocket-%20-333333?style=flat)
![JWT](https://img.shields.io/badge/JWT-%20-000000?style=flat&logo=jsonwebtokens&logoColor=white)

### Frontend
![Node.js](https://img.shields.io/badge/Node.js-20.14.0-339933?style=flat&logo=nodedotjs&logoColor=white)
![Vue.js](https://img.shields.io/badge/Vue.js-3-4FC08D?style=flat&logo=vuedotjs&logoColor=white)
![Vue Router](https://img.shields.io/badge/Vue%20Router-%20-4FC08D?style=flat&logo=vuedotjs&logoColor=white)
![Vuetify](https://img.shields.io/badge/Vuetify-3-1867C0?style=flat&logo=vuetify&logoColor=white)
![Axios](https://img.shields.io/badge/Axios-%20-5A29E4?style=flat&logo=axios&logoColor=white)
![SockJS Client](https://img.shields.io/badge/SockJS%20Client-%20-CC0000?style=flat)
![WebStomp Client](https://img.shields.io/badge/WebStomp%20Client-%20-6A1B9A?style=flat)
![event-source-polyfill](https://img.shields.io/badge/event--source--polyfill-%20-FFC107?style=flat)
![jwt-decode](https://img.shields.io/badge/jwt--decode-%20-000000?style=flat&logo=jsonwebtokens&logoColor=white)
![Vercel](https://img.shields.io/badge/Vercel-%20-000000?style=flat&logo=vercel&logoColor=white)

### Infra
![EC2](https://img.shields.io/badge/EC2-%20-FF9900?style=flat&logo=amazonec2&logoColor=white)
![ECR](https://img.shields.io/badge/ECR-%20-FF9900?style=flat&logo=amazonecr)
![CloudWatch](https://img.shields.io/badge/CloudWatch-%20-FF9900?style=flat&logo=amazoncloudwatch&logoColor=white)
![RDS](https://img.shields.io/badge/RDS-%20-527FFF?style=flat&logo=amazonrds&logoColor=white)
![ElastiCache](https://img.shields.io/badge/ElastiCache-%20-2D72D9?style=flat&logo=amazonelasticache&logoColor=white)
![Route 53](https://img.shields.io/badge/Route%2053-%20-8C4FFF?style=flat&logo=amazonroute53&logoColor=white)
![Nginx](https://img.shields.io/badge/Nginx-%20-009639?style=flat&logo=nginx&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-%20-2496ED?style=flat&logo=docker&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-%20-2088FF?style=flat&logo=githubactions&logoColor=white)
![Logstash](https://img.shields.io/badge/Logstash-8.10.0-000000?style=flat&logo=logstash&logoColor=white)
![Elasticsearch](https://img.shields.io/badge/Elasticsearch-8.10.0-005571?style=flat&logo=elasticsearch&logoColor=white)
![Kibana](https://img.shields.io/badge/Kibana-8.10.1-E8478B?style=flat&logo=kibana&logoColor=white)
![Slack](https://img.shields.io/badge/Slack-Webhook-4A154B?style=flat&logo=slack&logoColor=white)
![k6](https://img.shields.io/badge/k6-%20-7D64FF?style=flat&logo=k6&logoColor=white)


## 📈 Monitoring & Alerting  

<img src="https://github.com/kduoh99/chat-service/blob/main/images/logging/kibana-dashboard.png?raw=true" width="800"/>
<img src="https://github.com/kduoh99/chat-service/blob/main/images/logging/slack-alert.png?raw=true" width="800"/>


## 🧪 Load Testing

### Dummy Data & Scenario

- 10,000명의 회원을 생성하고, 하나의 그룹 채팅방에 10,000건의 메시지를 삽입
- 각 Virtual User는 로그인 → 그룹 채팅방 입장 → 메시지 내역 조회 → 메시지 전송 → 수신 여부 확인 순으로 시나리오를 수행

### Initial Load Metrics & Resource Monitoring

| Item | Image |
|:---:|:---:|
| k6 Overview | <img src="https://github.com/kduoh99/chat-service/blob/main/images/load-test/k6-dashboard-initial.png?raw=true" width="567"/><br><sub>초기 부하 테스트 응답 지표 - t3.small</sub> |
| Throughput | <img src="https://github.com/kduoh99/chat-service/blob/main/images/load-test/k6-analysis-tps.png?raw=true" width="567"/><br><sub>현재 시스템의 최대 Throughput은 8.1 TPS</sub> |
| Latency | <img src="https://github.com/kduoh99/chat-service/blob/main/images/load-test/k6-analysis-latency.png?raw=true" width="567"/><br><sub>최대 TPS 초과 이후 Latency 증가, 초과 전에는 평균 1초 내외 유지</sub> |
| Failures per Second | <img src="https://github.com/kduoh99/chat-service/blob/main/images/load-test/k6-analysis-fail.png?raw=true" width="567"/><br><sub>요청 실패가 발생한다면, 원인 분석 필요</sub> |
| System Resource Usage | <img src="https://github.com/kduoh99/chat-service/blob/main/images/load-test/cloudwatch-initial.png?raw=true" width="567"/><br><sub>EC2 CPU 사용률이 94.4%까지 상승 → 병목 지점으로 판단됨</sub> |

### Scale-Up / Scale-Out

| Instance Type | k6 Overview | System Resource Usage |
|:---:|:---:|:---:|
| t3.medium | <img src="https://github.com/kduoh99/chat-service/blob/main/images/load-test/k6-dashboard-t3medium.png?raw=true" width="305"/> | <img src="https://github.com/kduoh99/chat-service/blob/main/images/load-test/cloudwatch-t3medium.png?raw=true" width="305"/> |
| t3.large | <img src="https://github.com/kduoh99/chat-service/blob/main/images/load-test/k6-dashboard-t3large.png?raw=true" width="305"/> | <img src="https://github.com/kduoh99/chat-service/blob/main/images/load-test/cloudwatch-t3large.png?raw=true" width="305"/> |
| t3.xlarge | <img src="https://github.com/kduoh99/chat-service/blob/main/images/load-test/k6-dashboard-t3xlarge.png?raw=true" width="305"/> | <img src="https://github.com/kduoh99/chat-service/blob/main/images/load-test/cloudwatch-t3xlarge.png?raw=true" width="305"/> |
| t3.small × 2 | <img src="https://github.com/kduoh99/chat-service/blob/main/images/load-test/k6-dashboard-scaleout.png?raw=true" width="305"/> | <img src="https://github.com/kduoh99/chat-service/blob/main/images/load-test/cloudwatch-scaleout.png?raw=true" width="305"/><br> |

### Performance Summary

| Item | Image |
|:---:|:---:|
| Performance Comparison | <img src="https://github.com/kduoh99/chat-service/blob/main/images/load-test/result-summary-table.png?raw=true" width="560"/><br><sub>스케일 업·스케일 아웃 결과를 비교한 요약 표</sub> |
| Scale-Out Advantage | <img src="https://github.com/kduoh99/chat-service/blob/main/images/load-test/scaleout-reason-table.png?raw=true" width="560"/><br><sub>EC2 CPU 병목으로 메모리만 증가한 스케일 업은 효과가 미미했고, 스케일 아웃은 비용 대비 TPS 개선이 명확함</sub> |
