# pickup-request

## required background

- 각 점포는 비정기적으로 폐식용유를 배출하기 위해 수거/집하를 요청 합니다.
- 수거의 시작은 점주의 요청, 가맹사의 요청, 기사의 자발적인 수거 입니다.
- 기사는 폐식용유를 수거하고 라벨 정보를 기록 합니다.
- 점포별, 요청자별 수거 기록을 조회할 수 있습니다.

## required feature

### architecture

![image](https://github.com/user-attachments/assets/5b1bec5a-273b-44e4-a0d1-c60513476db3)

![image](https://github.com/user-attachments/assets/4109867a-f096-4e75-a990-d724ef0ce7a0)

### 구현에서 생략된 것

- 본 서버는 private vpc 에 존재하며, 인증은 gateway 에서 처리된 것으로 간주합니다.
- 라벨 정보에는 QR 코드, 수거된 용량, 이미지 등이 있지만, 이번 구현에서는 제외 됩니다.
- 점포 정보에는 주소 등이 있지만, admin 을 통해 사전에 등록된 것으로 간주 합니다.

### 시나리오에 관련된 actor 는 4 종류가 있으며, 시스템에 관련된 actor 는 2 종류가 있습니다.

- 가맹사 어드민: PARTNER_ADMIN
- 가맹사 점주: PARTNER_STORE_OWNER
- 수거 기사: PICKUP_DRIVER
- 본사 어드민: MASTER_ADMIN
- 자동 시스템: SYSTEM_AUTO
- 배치 시스템: SYSTEM_BATCH

### 수거 요청은 다음의 라이프사이클을 가집니다.

- REQUESTED -> ACCEPTED -> PROCESSED -> APPROVED -> COMPLETED
    - REQUESTED: 식별된 점포로 수거 요청이 들어온 상태.
    - ACCEPTED: 수거 요청이 수락한 상태
    - PROCESSED: 수거 요청이 수행되어 라벨이 기록된 상태
    - APPROVED: 수거 요청이 수행되어 승인된 상태
    - COMPLETED: 수거 요청이 완료된 상태
- 어느 상태에서든 CANCELED 가 가능 합니다.
    - 단 COMPLETED 된 것은 취소할 수 없습니다.
- 같은 식별번호의 수거 요청은 라이프사이클을 돌이킬 수 없습니다.

### 가맹사 점주의 수거 요청 scenario

1. 가맹사 점주: 점포를 지정하여 REQUESTED
2. 수거 기사: ACCEPTED
3. 수거 기사: 라벨을 기록하여 PROCESSED
4. 가맹사 점주: APPROVED
5. 자동 시스템: COMPLETED

### 가맹사 어드민의 수거 요청 scenario

1. 가맹사 어드민: 점포를 지정하여 REQUESTED
2. 수거 기사: ACCEPTED
3. 수거 기사: 라벨을 기록하여 PROCESSED
4. 가맹사 점주: APPROVED
5. 자동 시스템: COMPLETED

### 기사의 자발적인 수거 요청 scenario

> 가맹사 점주가 수거 요청을 하지 못하는 예외적인 케이스 입니다.
> - ex) 가맹사 점주 미등록/미승인, 가맹사 점주 단말기 파손

1. 수거 기사의 단말기에서 가맹점 점주가 인증 후 REQUESTED
2. 수거 기사: ACCEPTED
3. 수거 기사: 라벨을 기록하여 PROCESSED
4. 수거 기사의 단말기에서 가맹점 점주가 인증 후 APPROVED
5. 자동 시스템: COMPLETED
