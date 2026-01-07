# Authentication System

## Overview

The Holiday Analyzer application now includes a complete JWT-based authentication system for securing the admin panel.

## Features

- **JWT Token Authentication** - Secure, stateless authentication
- **Password Hashing** - BCrypt encryption for password security
- **Protected Routes** - Admin panel requires authentication
- **Session Management** - Token stored in localStorage
- **Auto-login** - Persistent sessions across browser refreshes

## Default Credentials

```
Username: admin
Password: admin123
```

**⚠️ IMPORTANT:** Change the default password in production!

## Architecture

### Backend Components

1. **User Entity** (`User.java`)
   - Stores user credentials and profile information
   - Fields: id, username, email, password (hashed), role, timestamps

2. **JWT Utility** (`JwtUtil.java`)
   - Generates and validates JWT tokens
   - Token expiration: 24 hours (configurable)
   - Secret key: Configurable via environment variable

3. **Authentication Filter** (`JwtAuthenticationFilter.java`)
   - Intercepts requests and validates JWT tokens
   - Extracts user information from token
   - Sets Spring Security context

4. **User Details Service** (`CustomUserDetailsService.java`)
   - Loads user details from database
   - Integrates with Spring Security

5. **Auth Controller** (`AuthController.java`)
   - `POST /api/auth/login` - Login endpoint
   - `GET /api/auth/validate` - Token validation endpoint

6. **Security Configuration** (`SecurityConfig.java`)
   - Configures Spring Security with JWT
   - Defines public and protected endpoints
   - Stateless session management

### Frontend Components

1. **Login Component** (`Login.tsx`)
   - Modern, responsive login form
   - Error handling and loading states
   - Redirects to admin panel on success

2. **Auth Context** (`AuthContext.tsx`)
   - Global authentication state management
   - Provides login/logout functions
   - Persists auth state in localStorage

3. **Protected Route** (`ProtectedRoute.tsx`)
   - Wrapper component for protected routes
   - Redirects to login if not authenticated

4. **Updated App.tsx**
   - React Router integration
   - Route configuration
   - Auth-aware navigation

## API Endpoints

### Public Endpoints

```
POST /api/auth/login
GET /api/countries/**
GET /api/subdivisions/**
GET /api/holidays/**
GET /api/school-holidays/**
GET /api/analysis/**
```

### Protected Endpoints (Require JWT Token)

```
POST /api/countries/**
PUT /api/countries/**
DELETE /api/countries/**
POST /api/holidays/**
PUT /api/holidays/**
DELETE /api/holidays/**
... (all POST/PUT/DELETE operations)
```

## Usage

### Login Flow

1. Navigate to `/login`
2. Enter credentials (default: admin/admin123)
3. On success, JWT token is stored in localStorage
4. User is redirected to `/admin`
5. All subsequent API requests include the token

### Making Authenticated Requests

```typescript
const token = localStorage.getItem('token');

fetch('http://localhost:8080/api/countries', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`
  },
  body: JSON.stringify(countryData)
});
```

### Logout

```typescript
// Clear authentication state
localStorage.removeItem('token');
localStorage.removeItem('username');
localStorage.removeItem('email');
localStorage.removeItem('role');

// Redirect to home
window.location.href = '/';
```

## Configuration

### Backend Environment Variables

```bash
# JWT Secret (minimum 256 bits)
JWT_SECRET=your-secret-key-change-in-production

# JWT Expiration (milliseconds, default: 24 hours)
JWT_EXPIRATION=86400000
```

Add to `.env` file:

```env
JWT_SECRET=holiday-analyzer-secret-key-change-in-production-minimum-256-bits
JWT_EXPIRATION=86400000
```

### Security Best Practices

1. **Change Default Password**
   - Create a new admin user with a strong password
   - Delete or disable the default admin account

2. **Use Strong JWT Secret**
   - Generate a random 256-bit key
   - Never commit secrets to version control
   - Use environment variables

3. **HTTPS in Production**
   - Always use HTTPS to prevent token interception
   - Configure secure cookies if needed

4. **Token Expiration**
   - Set appropriate expiration times
   - Implement token refresh if needed

5. **CORS Configuration**
   - Configure proper CORS policies
   - Restrict origins in production

## Database Schema

```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'ADMIN',
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
```

## Testing

### Manual Testing

1. **Login**
   ```bash
   curl -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username":"admin","password":"admin123"}'
   ```

2. **Validate Token**
   ```bash
   curl -X GET http://localhost:8080/api/auth/validate \
     -H "Authorization: Bearer YOUR_TOKEN_HERE"
   ```

3. **Access Protected Endpoint**
   ```bash
   curl -X POST http://localhost:8080/api/countries \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer YOUR_TOKEN_HERE" \
     -d '{"code":"US","name":"United States","population":331000000}'
   ```

## Troubleshooting

### "Invalid username or password"
- Check credentials are correct
- Verify user exists in database
- Check password is properly hashed

### "Token expired"
- Token has exceeded expiration time
- Login again to get a new token

### "Unauthorized" on protected endpoints
- Ensure token is included in Authorization header
- Format: `Bearer YOUR_TOKEN`
- Verify token is valid and not expired

### CORS errors
- Check backend CORS configuration
- Verify frontend URL is allowed
- Check browser console for details

## Future Enhancements

- [ ] Token refresh mechanism
- [ ] Password reset functionality
- [ ] Email verification
- [ ] Multi-factor authentication (MFA)
- [ ] Role-based access control (RBAC)
- [ ] User management UI
- [ ] Audit logging for authentication events
- [ ] Rate limiting for login attempts
- [ ] Account lockout after failed attempts

## Dependencies

### Backend
- Spring Security
- JJWT (Java JWT library) v0.12.3
- BCrypt password encoder

### Frontend
- React Router DOM v6.21.0
- React Context API for state management

## License

Part of the Holiday Analyzer project.
