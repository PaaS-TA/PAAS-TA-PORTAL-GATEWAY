# PAAS-TA-PORTAL-GATEWAY


## ZUUL Server
MSA의 문제점 중의 하나는 각 서비스가 다른 서버에 분리 배포 되기 때문에, API의 End point 즉, 서버의 URL이 각기 다르다는 것이다.
한 서비스에 한개 이상의 서버가 존재하기 때문에 이 서비스를 사용하는 클라이언트 입장에서는 다수의 end point가 생기게 되며, end point를 변경이 일어났을때, 
관리하기가 힘들다. 그래서 MSA 환경에서 서비스에 대한 도메인인 하나로 통합할 수 있는 API GATEWAY가 필요한 것이다.
API GATEWAY는 마치 프록시 서버 처럼 api들 앞에서 모든 api에 대한 end point를 통합하고, 인증 및 권한, 모니터링, logging 등의 기능을 제공하는 미들웨어로, 
서비스 지향 아키텍쳐(SOA, service Oriented Architecture)의 ESB (Enterprise Service Bus)의 경량화 버전이다.

Zuul은 백엔드에 대해 모든 요청을 처리하기 위한 Netflix의 API GATEWAY이다.
클라이언트에서 다양한 형태의 요청으로 인한 이슈를 방지하기 위해서 다양한 Filter를 사용하고 있다

Netflix Filter 기능
- 인증 및 보안(Authentication and Security) : 각 요청이 갖추어야 할 내용을 충족하지 못한 경우 해당 요청을 거부한다.
- 통찰력 및 모니터링(Insights and Monitoring) : 모든 트래픽이 지나기 때문에 의미있는 데이터와 지표를 수집할 수 있다. 
- 동적 라우팅(Dynamic Routing) : 필요에 따라 즉시 원하는 백엔드 클러스터로 트래픽을 보내고 끊을 수 있다. 
- 부하 테스트(Stress Testing) : 신규 추가한 서비스 또는 백엔드에 트래픽을 점진적으로 증가하는 등의 방식으로 부하를 유발할 수 있다. 
- 트래픽 드랍(Load shedding) : 각 요청에 대해 제한된 이상의 요청이 발생한 경우 이를 드랍하는 방식을 사용할 수 있다. 
- 정적 응답 처리(Static Reponse handling) : 특정 요청에 대해서는 백엔드로 트래픽을 보내는 대신 즉시 응답하도록 구성할 수 있다. 
- 멀티 리전 고가용성(Multiregion Resiliency) : Zuul 은 받은 요청을 아마존 웹 서비스의 리전 레벨에서 분배할 수 있다

## 유의사항

개발 정보
- java 1.8 버전
- SpringCloud Edgware.RELEASE 
- TomcatEmded 8.5.14
- Gradle 4.4.1
- Spring-boot 1.5.9
- Redis 1.3.1


