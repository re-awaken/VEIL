# Testing Queries - Users Table

## Single-column indexes

1. Primary key lookup (B-tree on `id`)
   ```sql
   SELECT * FROM users WHERE id = 42;
   ```

2. Unique email lookup (B-tree/unique on `email`)
   ```sql
   SELECT id, first_name, last_name, email FROM users WHERE email = 'user_10@example.com';
   ```

3. Status filter (B-tree on `status`)
   ```sql
   SELECT id, first_name, last_name, status FROM users WHERE status = 'active';
   ```

4. Age range (B-tree on `age`)
   ```sql
   SELECT id, first_name, last_name, age FROM users WHERE age BETWEEN 25 AND 35;
   ```

5. Created-at range (B-tree on `created_at`)
   ```sql
   SELECT id, email, created_at FROM users WHERE created_at >= NOW() - interval '30 days';
   ```

6. First name equality (B-tree on `first_name`)
   ```sql
   SELECT * FROM users WHERE first_name = 'Sarah';
   ```

7. Last name equality (B-tree on `last_name`)
   ```sql
   SELECT * FROM users WHERE last_name = 'Taylor';
   ```

## Composite indexes

8. Status + age (composite B-tree on `(status, age)`)
   ```sql
   SELECT * FROM users WHERE status = 'active' AND age > 30;
   ```

9. Created-at + status (composite B-tree on `(created_at DESC, status)`)
   ```sql
   SELECT * FROM users WHERE created_at >= NOW() - interval '90 days' AND status = 'pending';
   ```

10. Last name + first name (composite B-tree on `(last_name, first_name)`)
    ```sql
    SELECT * FROM users WHERE last_name = 'Clark' AND first_name = 'David';
    ```

## Ordering and LIMIT

11. Order by created_at DESC (B-tree on `created_at`)
    ```sql
    SELECT * FROM users ORDER BY created_at DESC LIMIT 20;
    ```

12. Order by age ASC (B-tree on `age`)
    ```sql
    SELECT * FROM users ORDER BY age ASC LIMIT 10;
    ```

13. Order by last_name, first_name (composite B-tree)
    ```sql
    SELECT * FROM users ORDER BY last_name, first_name LIMIT 25;
    ```

## Text / pattern matching

14. Prefix search on first_name (B-tree with `text_pattern_ops` or pg_trgm)
    ```sql
    SELECT * FROM users WHERE first_name LIKE 'Sar%';
    ```

15. Case-insensitive search (lower expression index)
    ```sql
    SELECT * FROM users WHERE LOWER(first_name) = 'sarah';
    ```

16. ILIKE pattern (pg_trgm / GIN index)
    ```sql
    SELECT * FROM users WHERE last_name ILIKE '%mitch%';
    ```

## Aggregations

17. Count by status (index on `status`)
    ```sql
    SELECT status, COUNT(*) AS total_users FROM users GROUP BY status;
    ```

18. Average age by status (composite index on `(status, age)`)
    ```sql
    SELECT status, AVG(age) AS avg_age FROM users GROUP BY status;
    ```

19. Min/max created_at (B-tree on `created_at`)
    ```sql
    SELECT MIN(created_at) AS oldest, MAX(created_at) AS newest FROM users;
    ```

## CTE

20. CTE: active users over age 30
    ```sql
    WITH active_adults AS (
        SELECT id, first_name, last_name, age
        FROM users
        WHERE status = 'active' AND age > 30
    )
    SELECT * FROM active_adults ORDER BY age DESC;
    ```

## Window functions

21. Age ranking across all users
    ```sql
    SELECT id, first_name, last_name, age,
           RANK() OVER (ORDER BY age DESC) AS age_rank
    FROM users;
    ```

22. Row number partitioned by status
    ```sql
    SELECT id, first_name, last_name, status, age,
           ROW_NUMBER() OVER (PARTITION BY status ORDER BY age DESC) AS status_age_rank
    FROM users;
    ```

## Mixed conditions

23. Multiple conditions with GROUP BY and HAVING
    ```sql
    SELECT
        status,
        COUNT(*) AS user_count,
        AVG(age) AS average_age
    FROM users
    WHERE age >= 18
    GROUP BY status
    HAVING COUNT(*) > 5
    ORDER BY user_count DESC;
    ```

24. Full-text search across names (GIN/GiST on `to_tsvector`)
    ```sql
    SELECT id, first_name, last_name
    FROM users
    WHERE to_tsvector('english', first_name || ' ' || last_name) 
          @@ plainto_tsquery('english', 'David Clark');
    ```

25. Sequential scan force (for comparison)
    ```sql
    SELECT * FROM users WHERE LOWER(first_name) = 'raj';
    ```