#!/bin/bash

# Claude PR - Automated Pull Request Creation Script
# This script automatically creates a PR with title and body generated from current branch changes

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Helper functions
error() {
    echo -e "${RED}❌ Error: $1${NC}" >&2
    exit 1
}

success() {
    echo -e "${GREEN}✅ $1${NC}"
}

warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

# Check if we're in a git repository
if ! git rev-parse --git-dir >/dev/null 2>&1; then
    error "Not in a git repository"
fi

# Check if gh CLI is installed
if ! command -v gh &> /dev/null; then
    error "GitHub CLI (gh) is not installed. Please install it first: https://cli.github.com/"
fi

# Check if user is authenticated with GitHub CLI
if ! gh auth status >/dev/null 2>&1; then
    error "Not authenticated with GitHub CLI. Run 'gh auth login' first."
fi

# Get current branch name
CURRENT_BRANCH=$(git branch --show-current)

if [ "$CURRENT_BRANCH" = "main" ] || [ "$CURRENT_BRANCH" = "production" ]; then
    error "Cannot create PR from main or production branch. Switch to a feature branch first."
fi

info "Current branch: $CURRENT_BRANCH"

# Check if there are any commits ahead of main
COMMITS_AHEAD=$(git rev-list --count main..HEAD)
if [ "$COMMITS_AHEAD" -eq 0 ]; then
    error "No commits ahead of main branch. Make some changes first."
fi

info "Found $COMMITS_AHEAD commit(s) ahead of main"

# Get commit messages for PR description
COMMIT_MESSAGES=$(git log main..HEAD --pretty=format:"- %s")

# Extract ticket number from branch name if exists
TICKET_NUMBER=""
if [[ $CURRENT_BRANCH =~ ^[a-z]+/([A-Z]+-[0-9]+) ]]; then
    TICKET_NUMBER="${BASH_REMATCH[1]}"
fi

# Generate PR title
if [ -n "$TICKET_NUMBER" ]; then
    # Get the first commit message and format it
    FIRST_COMMIT=$(git log -1 --pretty=format:"%s" HEAD)
    # Remove ticket number from commit message if present
    FIRST_COMMIT=$(echo "$FIRST_COMMIT" | sed -E "s/^\[[A-Z]+-[0-9]+\] //")
    PR_TITLE="[$TICKET_NUMBER] $FIRST_COMMIT"
else
    # Use the first commit message as title
    PR_TITLE=$(git log -1 --pretty=format:"%s" HEAD)
fi

# Generate PR body based on template structure
PR_BODY=$(cat <<EOF
## 작업 배경
-

## 작업 내용
$COMMIT_MESSAGES

## 참고 사항
-

<!-- PR Guide

- Reviewer 분들은 코드 리뷰 시 좋은 코드의 방향을 제시하되, 코드 수정을 강제하지 말아 주세요.
- Reviewer 분들은 좋은 코드를 발견한 경우, 칭찬과 격려를 아끼지 말아 주세요.
- Pn룰을 적극적으로 활용해주세요.
    - P1: 꼭 반영해주세요 (Request changes)
        - 보안 취약점, 심각한 버그, 중대한 로직 오류
    - P2: 적극적으로 고려해주세요 (Request changes)
        - 성능 이슈, 확장성 문제, 잠재적 버그 가능성
    - P3: 웬만하면 반영해 주세요 (Comment)
        - 코드 구조 개선, 테스트 케이스 추가, 예외 처리 보완
    - P4: 반영해도 좋고 넘어가도 좋습니다 (Approve)
        - 변수명 개선 제안, 메서드 분리 제안
    - P5: 사소한 의견입니다 (Approve)
        - 오타 수정, 들여쓰기/공백 조정, 간단한 코드 스타일 수정
    - ask: 질문이 있습니다 (Comment)
        - 코드 이해가 안되는 부분, 구현 의도 파악이 어려운 부분

-->
EOF
)

# Show preview
echo ""
info "PR Preview:"
echo -e "${BLUE}Title:${NC} $PR_TITLE"
echo -e "${BLUE}Body:${NC}"
echo "$PR_BODY"
echo ""

# Confirm before creating
read -p "Do you want to create this PR? (y/N): " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    warning "PR creation cancelled"
    exit 0
fi

# Check if remote branch exists, create if not
if ! git ls-remote --exit-code --heads origin "$CURRENT_BRANCH" >/dev/null 2>&1; then
    info "Pushing branch to origin..."
    git push -u origin "$CURRENT_BRANCH"
fi

# Create the PR
info "Creating pull request..."
PR_URL=$(gh pr create --title "$PR_TITLE" --body "$PR_BODY" --base main --head "$CURRENT_BRANCH")

success "Pull request created successfully!"
success "URL: $PR_URL"

# Open PR in browser (optional)
read -p "Open PR in browser? (y/N): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    gh pr view --web "$CURRENT_BRANCH"
fi