## Spring Boot Standard Reference (Product API)
본 프로젝트는 단일 엔티티인 Product를 기반으로 확장성 및 유지보수성을 고려한 표준 CRUD API 템플릿 프로젝트입니다.

### 🔌 핵심 구현 기술
1. 전역 예외 처리
   - **CommonException**: 모든 커스텀 예외의 최상위 클래스, 이를 상속받아 비즈니스 예외 계층 구조화
   - **CommonControllerAdvice**: `@ControllerAdvice` 사용, 애플리케이션 전역에서 발생하는 예외 일괄 처리
   - **ErrorResponse**: 에러 코드, 메시지 등 규격화된 JSON 응답 포맷 제공, 클라이언트와의 통신 표준 준수
3. 도메인 및 쿼리 설계
   - **Builder 패턴**: 객체 생성 시 가독성을 높이고 필드 설정의 유연성 확보
   - **QueryDSL**: Type-safe한 쿼리 작성 및 Pageable을 활용한 효율적인 페이징 처리 구현
   - **CRUD API**: 표준 REST API 규격에 맞춘 제품 관리 기능 구현
4. API 문서 자동화
   - **Spring Rest Docs**: 테스트 코드 실행 결과와 연동하여 신뢰도 높은 API 문서(AsciiDoc) 자동 생성
   - **설정 커스터마이징**: 스니펫(Snippet) 관리를 통한 API 명세 세분화

### ⚡️ 테스트 전략
1. 현황
   | 분류 (Scope) | 커버리지 (Coverage) | 비고 (Notes) |
   |:------|:---:|:---|
   | 클래스 (Classes) | 100% | 모든 도메인, 서비스, 컨트롤러 클래스 검증 완료 |
   | 라인 (Lines) | 97% | 분기문 및 예외 케이스 포함 핵심 로직 대부분 검증 |
   | 메서드 (Methods) | 100% | 작성된 모든 메서드에 대한 테스트 케이스 확보 |

3. 세부 내역
- Total Cases: 24개 (단위 및 통합 테스트)
- IntelliJ 내장 Coverage 도구 활용
- ProductControllerTest: `@AutoConfigureMockMvc`, `@Transactional`을 사용하여 API 엔드포인트 및 전역 예외 응답 처리 검증
- ProductServiceTest: `@SpringBootTest`를 활용한 서비스 레이어 통합 테스트 수행
- Documentation: `@ExtendWith(RestDocumentationExtension.class)`를 통한 문서 스니펫 추출.
- 핵심 검증 대상: 상품 CRUD 로직, QueryDSL 페이징 처리, `CommonException` 기반 전역 예외 응답 정합성 확인
