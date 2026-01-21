# Database Migrations with Flyway

This directory contains Flyway database migration scripts for the Holiday Analyzer application.

## 📋 12-Factor App Compliance

This implements **Factor XII: Admin Processes** - database migrations are run as one-off processes, separate from the application runtime.

## 🗂️ Migration Files

Flyway migrations follow the naming convention: `V{version}__{description}.sql`

### Current Migrations:

- **V1__Initial_schema.sql**: Creates all database tables and relationships
  - Countries table
  - Subdivisions table
  - Holidays table
  - School holidays table
  - Users table
  - Indexes for performance
  
- **V2__Seed_initial_data.sql**: Seeds critical initial data
  - Default admin user (username: admin, password: admin123)

## 🚀 How Flyway Works

1. **Automatic Execution**: Migrations run automatically on application startup
2. **Version Tracking**: Flyway tracks which migrations have been applied in `flyway_schema_history` table
3. **Idempotent**: Each migration runs only once
4. **Ordered**: Migrations run in version order (V1, V2, V3, etc.)

## 📝 Creating New Migrations

When you need to change the database schema:

1. Create a new file: `V{next_version}__{description}.sql`
   - Example: `V3__Add_user_preferences_table.sql`
   
2. Write your SQL changes:
   ```sql
   -- Add new column
   ALTER TABLE users ADD COLUMN last_login TIMESTAMP;
   
   -- Create new table
   CREATE TABLE user_preferences (
       id BIGSERIAL PRIMARY KEY,
       user_id BIGINT NOT NULL,
       theme VARCHAR(20) DEFAULT 'light',
       CONSTRAINT fk_preferences_user FOREIGN KEY (user_id) REFERENCES users(id)
   );
   ```

3. Restart the application - Flyway will apply the new migration automatically

## ⚙️ Configuration

Flyway is configured in `application.properties`:

```properties
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration
spring.flyway.validate-on-migrate=true
```

## 🔧 Important Notes

### Hibernate DDL is Disabled
- `spring.jpa.hibernate.ddl-auto=none` in all profiles
- Flyway has full control over schema management
- This ensures consistent schema across all environments

### DataLoader.java
- Still used for loading business data (countries, holidays, etc.)
- Runs after Flyway migrations complete
- Checks if data exists before inserting (idempotent)

### Production Deployment
- Migrations run automatically on first deployment
- Existing databases are baselined (Flyway starts tracking from current state)
- New migrations apply incrementally

## 🐛 Troubleshooting

### Migration Failed
If a migration fails:
1. Fix the SQL in the migration file
2. Delete the failed entry from `flyway_schema_history` table:
   ```sql
   DELETE FROM flyway_schema_history WHERE version = 'X';
   ```
3. Restart the application

### Reset Database (Development Only)
To start fresh:
```bash
# Drop and recreate database
docker-compose down -v
docker-compose up -d postgres

# Restart backend - Flyway will recreate everything
docker-compose up backend
```

## 📚 Best Practices

1. **Never modify existing migrations** - create new ones instead
2. **Test migrations locally** before committing
3. **Keep migrations small** - one logical change per file
4. **Use transactions** - Flyway wraps each migration in a transaction
5. **Add rollback scripts** (optional) - create `U{version}__*.sql` for undo operations

## 🔗 Resources

- [Flyway Documentation](https://flywaydb.org/documentation/)
- [Spring Boot Flyway Integration](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.data-initialization.migration-tool.flyway)
