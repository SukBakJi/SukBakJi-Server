# SukBakJi-Server

**Spring Boot 버전:** 3.3.1

**Java 버전:** 21

## 협업 규칙
### 기능 단위 issue 생성
  ```
  제목: [commit type(아래 참고)] 게시글 삭제 기능 구현 
  
  ## :loudspeaker: 이슈 내용 
  > 간단한내용...
  
  ## :page_with_curl: 상세 내용
  - 상세내용...
  
  ## :heavy_check_mark: 체크리스트
  - [ ] 완료할 기능...
  - [x] 완료한 기능...
  
  ## :round_pushpin: 참고 내용
  ```

### Branch 규칙 지키기
- main : 최종 런칭 버전을 개발하는 **메인 브랜치**
- develop: 기능 개발 후 머지할 브랜치 
- feat: 기능을 개발하는 브랜치 (ex : feat/login)
- bugfix : 최종 런칭 버전개발 과정에서 발생한 버그를 수정하는 브랜치

### Commit Message
**💡커밋 메시지는 `기능` 단위로 작성해주세요.**

EX) #이슈번호 [commit type]: 기능 내용

#### Commit Type
- feat: 새 기능 구현
- fix: 에러 수정
- style: 코드 변경이 없는 경우, 코드 포멧팅
- refactor: 코드 리팩토링
- docs: 문서 수정 및 생성
- rename: 파일, 폴더명만 수정하거나 이동하는 작업
- test: 테스트 관련 작업