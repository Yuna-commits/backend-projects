# Task Tracker CLI

[Task Tracker](https://roadmap.sh/projects/task-tracker)의 요구사항을 기반으로 구현한 **CLI 기반 작업 관리 도구**입니다.

## 📋 Features

- **Add Task**: 새로운 작업을 추가하고 고유 ID를 생성합니다.
- **Update Task**: 기존 작업의 설명을 수정합니다.
- **Delete Task**: 작업을 삭제합니다.
- **Mark Status**: 작업 상태를 `In Progress` 또는 `Done`으로 변경합니다.
- **List Tasks**: 저장된 모든 작업을 조회합니다.
- **Filter Tasks**: 상태별(`todo`, `in-progress`, `done`)로 작업을 필터링하여 조회합니다.
- **Data Persistence**: `tasks.json` 파일을 통해 데이터를 영구적으로 저장합니다.

## 🛠️ Implementation Constraints & Tech Stack

- **Language**: Java 17
- **Build Tool**: Maven

* **No External Libraries**:
  - `Gson`이나 `Jackson` 대신 `java.util.regex`를 사용하여 커스텀 JSON 파서 구현
  - `java.nio.file` 패키지를 사용하여 파일 입출력 처리
* **Architecture**:
  - `CLI` (입출력) - `Service` (비즈니스 로직) - `Repository` (데이터 처리) 계층 분리

## 🚀 Getting Started

### 1. Build

```bash
mvn clean package
```

### 2. Run

1. Java 명령어로 실행:

```bash
java -cp target/classes tasktracker.Main [command] [arguments]
```

2. 스크립트로 실행 (Windows): 프로젝트 루트의 tasktracker.bat 파일 사용

```bash
./tasktracker add "Buy groceries"
```

## 📂 Project Structure

```
src/main/java/tasktracker
├── Main.java              # Entry Point
├── cli/                   # CLI Input Handling & View
├── service/               # Business Logic
├── repository/            # File I/O & Custom JSON Parser
├── model/                 # Data Class (Record) & Enum
└── exception/             # Custom Exceptions
```

## 📖 Usage Examples

```
$ ./tasktracker list

+-------+------------------------------------------+--------------+------------------+------------------+
| ID    | Description                              | Status       | Created At       | Updated At       |
+-------+------------------------------------------+--------------+------------------+------------------+
| 1     | Finish documentation                     | todo         | 2026-01-29 22:41 | 2026-01-29 22:41 |
| 2     | Implement CLI                            | todo         | 2026-01-29 22:41 | 2026-01-29 22:41 |
+-------+------------------------------------------+--------------+------------------+------------------+
```
