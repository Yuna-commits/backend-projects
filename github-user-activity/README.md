# GitHub User Activity CLI

[GitHub User Activity](https://roadmap.sh/projects/github-user-activity)의 요구사항을 기반으로 구현한 **CLI 기반 GitHub 활동 조회 도구**입니다.

터미널에서 특정 사용자의 최근 GitHub 활동 내역을 보여줍니다.

## 📋 Features

- **Fetch Activity**: 특정 GitHub 사용자의 최근 이벤트 데이터를 조회합니다.
- **Event Formatting**: 이벤트 타입(`Push`, `Issues`, `Watch`, `Create`, `PullRequest` 등)에 따라 적절한 문장으로 가공하여 출력합니다.

## 🛠️ Implementation Constraints & Tech Stack

- **Language**: Java 17
- **Build Tool**: Maven

* **No External Libraries**:
  - `Gson`이나 `Jackson` 대신 `java.util.regex`를 사용하여 중첩된 구조를 처리하느 커스텀 JSON 파서 구현
  - 외부 통신 라이브러리 없이 Java 내장 `java.net.http.HttpClient`를 사용하여 REST API 통신 구현
* **Architecture**:
  - `CLI` (입출력) - `Service` (비즈니스 로직 및 포맷팅) - `Network` (API 통신 및 파싱) 계층 분리

## 🚀 Getting Started

### 1. Build

```bash
mvn clean package
```

### 2. Run

1. Java 명령어로 실행:

```bash
java -cp target/classes github_user_activity.Main [username]
```

2. 스크립트로 실행 (Windows): 프로젝트 루트의 tasktracker.bat 파일 사용

```bash
./github-activity username
```

## 📂 Project Structure

```
src/main/java/tasktracker
├── Main.java              # Entry Point
├── cli/                   # CLI Input Handling & View
├── service/               # Business Logic & Event Formatting
├── network/               # HTTP Client & Custom JSON Parser
├── model/                 # Data Class (Record) & Enum
└── exception/             # Custom Exceptions
```

## 📖 Usage Examples

```
$ ./github-activity Yuna-commits

Output:
- Pushed 1 commits to Bit-gram/bitgram-frontend
- Merged a pull request in bitgram-frontend
- Opened a pull request in bitgram-backend
- Created branch in Bit-gram/bitgram-backend
```
