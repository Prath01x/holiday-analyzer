# Git Workflow & Branching Strategy

## 📋 Branch Structure

### Main Branches

- **`main`** - Production-ready code
  - Always stable and deployable
  - Only accepts merges from `develop` or hotfix branches
  - Protected branch (requires pull request)

- **`develop`** - Integration branch for features
  - Latest development changes
  - Base branch for all feature branches
  - Merged into `main` for releases

### Supporting Branches

- **`feature/*`** - New features or enhancements
  - Branch from: `develop`
  - Merge back to: `develop`
  - Naming: `feature/holiday-api`, `feature/dashboard-ui`

- **`bugfix/*`** - Bug fixes during development
  - Branch from: `develop`
  - Merge back to: `develop`
  - Naming: `bugfix/fix-database-connection`

- **`hotfix/*`** - Critical production fixes
  - Branch from: `main`
  - Merge back to: `main` AND `develop`
  - Naming: `hotfix/fix-critical-error`

---

## 🔄 Workflow Rules

### 1. Starting New Work

```bash
# Always start from develop
git checkout develop
git pull origin develop

# Create feature branch
git checkout -b feature/your-feature-name
```

### 2. Working on Features

```bash
# Make your changes
# Commit frequently with meaningful messages
git add .
git commit -m "feat: add holiday entity model"

# Push to remote
git push origin feature/your-feature-name
```

### 3. Merging Features

```bash
# Update your branch with latest develop
git checkout develop
git pull origin develop
git checkout feature/your-feature-name
git merge develop

# Resolve conflicts if any
# Push updated branch
git push origin feature/your-feature-name

# Create Pull Request on GitHub/GitLab
# After review and approval, merge to develop
```

### 4. Creating a Release

```bash
# Merge develop into main
git checkout main
git pull origin main
git merge develop
git push origin main

# Tag the release
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0
```

---

## 📝 Commit Message Convention

Use conventional commits format:

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Types

- **feat**: New feature
- **fix**: Bug fix
- **docs**: Documentation changes
- **style**: Code style changes (formatting, no logic change)
- **refactor**: Code refactoring
- **test**: Adding or updating tests
- **chore**: Maintenance tasks (dependencies, build config)
- **perf**: Performance improvements

### Examples

```bash
feat(backend): add Country entity and repository

- Created Country JPA entity
- Added CountryRepository interface
- Configured database relationships

Closes #12

---

fix(frontend): resolve API connection timeout

- Increased axios timeout to 30s
- Added retry logic for failed requests

Fixes #45

---

docs: update README with deployment instructions

---

chore(deps): update Spring Boot to 3.2.1
```

---

## 🚫 Branch Protection Rules

### `main` branch
- ✅ Require pull request reviews (at least 1 approval)
- ✅ Require status checks to pass
- ✅ No direct pushes allowed
- ✅ Require linear history

### `develop` branch
- ✅ Require pull request reviews
- ✅ Allow force pushes (for maintainers only)

---

## 🔀 Pull Request Guidelines

### Before Creating PR

1. ✅ Update your branch with latest `develop`
2. ✅ Run all tests locally
3. ✅ Ensure code builds successfully
4. ✅ Review your own changes first

### PR Template

```markdown
## Description
Brief description of changes

## Type of Change
- [ ] New feature
- [ ] Bug fix
- [ ] Documentation update
- [ ] Refactoring

## Testing
- [ ] Tested locally
- [ ] Added/updated tests
- [ ] All tests pass

## Checklist
- [ ] Code follows project style guidelines
- [ ] Self-review completed
- [ ] Documentation updated
- [ ] No breaking changes (or documented)

## Related Issues
Closes #issue_number
```

---

## 🎯 Quick Reference

### Daily Development

```bash
# Start your day
git checkout develop
git pull origin develop

# Create feature branch
git checkout -b feature/my-feature

# Work and commit
git add .
git commit -m "feat: implement feature"
git push origin feature/my-feature

# Create PR on GitHub
# After merge, delete feature branch
git branch -d feature/my-feature
git push origin --delete feature/my-feature
```

### Keeping Branch Updated

```bash
# While on feature branch
git fetch origin
git merge origin/develop

# Or use rebase (cleaner history)
git rebase origin/develop
```

### Undo Last Commit (not pushed)

```bash
git reset --soft HEAD~1
```

### Undo Changes in File

```bash
git checkout -- filename
```

---

## 🏷️ Version Tagging

Use Semantic Versioning: `MAJOR.MINOR.PATCH`

- **MAJOR**: Breaking changes
- **MINOR**: New features (backward compatible)
- **PATCH**: Bug fixes

```bash
# Create tag
git tag -a v1.2.3 -m "Release version 1.2.3"

# Push tag
git push origin v1.2.3

# List tags
git tag -l
```

---

## ⚠️ Important Rules

1. **NEVER commit directly to `main`**
2. **NEVER commit directly to `develop`** (use feature branches)
3. **ALWAYS pull before pushing**
4. **ALWAYS create PR for code review**
5. **NEVER commit sensitive data** (passwords, API keys)
6. **ALWAYS write meaningful commit messages**
7. **DELETE feature branches after merge**

---

## 🆘 Emergency Hotfix

```bash
# Critical bug in production
git checkout main
git pull origin main
git checkout -b hotfix/critical-bug-fix

# Fix the bug
git add .
git commit -m "hotfix: fix critical production bug"
git push origin hotfix/critical-bug-fix

# Create PR to main
# After merge to main, also merge to develop
git checkout develop
git merge main
git push origin develop
```

---

## 👥 Team Collaboration

### Code Review Checklist

**For Reviewers:**
- ✅ Code quality and readability
- ✅ Follows project conventions
- ✅ Tests are included
- ✅ No security vulnerabilities
- ✅ Performance considerations
- ✅ Documentation updated

**For Authors:**
- ✅ Respond to all comments
- ✅ Make requested changes
- ✅ Re-request review after updates
- ✅ Thank reviewers

---

## 📚 Additional Resources

- [Git Documentation](https://git-scm.com/doc)
- [Conventional Commits](https://www.conventionalcommits.org/)
- [Semantic Versioning](https://semver.org/)
- [GitHub Flow](https://guides.github.com/introduction/flow/)

---

## 🎓 Project-Specific Notes

**Current Setup:**
- ✅ Initial project setup committed to `main`
- ⏳ Next: Create `develop` branch
- ⏳ All future work in feature branches

**Team Members:**
- Lucas [Matrikelnummer]
- [Name 2] [Matrikelnummer]
- [Name 3] [Matrikelnummer]

---

*Last Updated: November 30, 2024*
