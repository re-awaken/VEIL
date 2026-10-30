Backend is done with connections regarding request mapping

**A. SQL Query Editor**
- Provide a clean SQL editor with syntax highlighting if feasible.
- Include an Analyze Query button.
- Send the entered SQL to `POST /api/analyze`.
- Display validation errors and API errors clearly.

**B. Query Analysis Results**
Display the response from `/api/analyze`, including:
- SQL tokens
- Abstract Syntax Tree (AST)
- Table name
- Selected columns
- WHERE condition
- Optimization recommendations

**C. PostgreSQL Database Panel**
Integrate `GET /api/database`.

Display:
- Connected database name
- Available tables
- Available indexes
- A refresh button to reload database metadata

Use actual API results rather than hardcoded or placeholder database information.

**D. Execution Plan Analyzer**
Integrate `POST /api/explain` with a request body containing the SQL query.

Display the execution plan as a readable tree, including:
- Root operation and nested plan nodes
- Node type, such as `Seq Scan`, `Index Scan`, `Index Only Scan`, or `Nested Loop`
- Table/relation name and index name
- Estimated rows
- Startup cost and total cost
- Estimated row width
- Scan direction and strategy, when available
- Parallel-aware status

The backend currently returns the plan under the `plan` field, with nested nodes in `children`.

**E. Plan Metrics Dashboard**
Display the plan-wide metrics returned by the backend:
- Total plan nodes
- Scan node count
- Index-related scan count
- Sequential scan count
- Human-readable plan summary

Show estimated costs as PostgreSQL planner cost units, not milliseconds.

**F. Recommendations Panel**
Display recommendations from the execution-plan analysis.
- Show aggregated root-level recommendations.
- Avoid duplicate messages.
- Make recommendations easy to read.
- Distinguish informational findings from potential optimization opportunities.

### 3. API integration

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/database` | Database name, tables, and indexes |
| POST | `/api/analyze` | SQL tokens, AST, query details, and recommendations |
| POST | `/api/explain` | PostgreSQL execution plan and plan metrics |

POST requests should use JSON with this structure:

`{"sql": "SELECT * FROM users WHERE id = 1;"}`

Use the actual response fields returned by the backend. Handle loading states, empty results, malformed SQL, and server/database errors.

### 4. UI/UX expectations
- Clean, modern, developer-focused dashboard.
- Responsive design for desktop and mobile.
- SQL editor and results should be easy to navigate.
- Use expandable/collapsible execution-plan nodes.
- Use cards or compact metric panels for plan statistics.
- Provide readable empty, loading, success, and error states.
- Avoid fake database status or fabricated performance metrics.

### 5. Technical constraints
- The existing frontend is currently at `src/main/resources/static/index.html`.
- Keep the existing backend and PostgreSQL configuration intact.
- Don't add authentication or JWT.
- Don't execute SQL queries automatically when the user only requests analysis.
- Keep the frontend modular so it can be maintained and extended later.
- Coordinate any API contract changes before implementing them.

### 6. Acceptance criteria
The frontend will be considered complete when:
1. Database metadata loads from the real PostgreSQL database.
2. SQL analysis results render correctly.
3. Execution plans display recursively as a tree.
4. Plan metrics match the backend response.
5. Recommendations are displayed without duplication.
6. Errors and loading states are handled properly.
7. The interface works on desktop and mobile.
