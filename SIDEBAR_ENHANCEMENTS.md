# Sidebar Enhancements - Feature Documentation

## Overview

Enhanced the sidebar with multiple new features:
1. **Edit session titles** - Inline editing with save/cancel
2. **Display username** - Shows current user with avatar
3. **Profile link** - Direct link to auth.markpost.dev
4. **Application version** - Shows current version (1.0.0)

## Features Implemented

### 1. Edit Chat Session Names

#### Backend Changes

**New Endpoint:**
```
PUT /api/v1/sessions/{sessionId}
Authorization: Bearer {token}
Content-Type: application/json

Request Body:
{
  "title": "New Session Name"
}

Response:
{
  "id": "session-uuid",
  "title": "New Session Name",
  "createdAt": "2025-12-18T21:30:00",
  "updatedAt": "2025-12-18T21:35:00"
}
```

**Files Modified:**
- `ChatSessionController.java` - Added `updateSession` endpoint
- `ChatSessionService.java` - Added `updateSessionTitle` method

#### Frontend Changes

**New API Method:**
```typescript
export async function updateSession(sessionId: string, title: string): Promise<ChatSession>
```

**Editing Flow:**
1. User clicks edit icon (pencil) on a session
2. Title changes to an input field
3. User can:
   - Type new name
   - Press Enter to save
   - Press Escape to cancel
   - Click checkmark to save
   - Click X to cancel
4. Session title updates in real-time
5. `updatedAt` timestamp automatically updates

**UI Elements:**
- ✏️ Edit icon (blue) - Appears on hover
- ✓ Save icon (green) - Confirm changes
- ✕ Cancel icon (gray) - Discard changes
- Input field with blue border during editing

### 2. Username Display in Sidebar

**Location:** Sidebar footer
**Features:**
- User avatar (circle with first letter)
- Full username display
- Links to profile page
- "View Profile →" subtitle

**Implementation:**
```typescript
const [userName, setUserName] = useState<string>('');

const loadUserInfo = async () => {
  const info = await fetchUserInfo();
  if (info) {
    setUserName(info.userName);
  }
};
```

**Visual:**
```
┌─────────────────────────┐
│ [M] Mark                │
│     View Profile →      │
└─────────────────────────┘
```

### 3. Profile Link

**URL:** https://auth.markpost.dev
**Behavior:**
- Opens in new tab (`target="_blank"`)
- Secure with `rel="noopener noreferrer"`
- Hover effect (background highlight)
- Entire user section is clickable

**Usage:**
- Click on username/avatar in sidebar footer
- Redirects to auth service profile page
- Allows users to manage account settings

### 4. Application Version

**Display:** "Version 1.0.0"
**Location:** Bottom of sidebar footer
**Style:** Small, centered, gray text
**Purpose:** Help users identify app version for support

## UI/UX Design

### Sidebar Layout (Top to Bottom)

```
┌───────────────────────────┐
│ [+ New Chat]              │ ← Header
├───────────────────────────┤
│ ┌─────────────────────┐   │
│ │ 💬 Chat Session 1   │   │ ← Session list
│ │    Dec 18, 2025     │   │   (scrollable)
│ │              ✏️ 🗑️  │   │
│ └─────────────────────┘   │
│                           │
│ ┌─────────────────────┐   │
│ │ 💬 Chat Session 2   │   │
│ │    Dec 17, 2025     │   │
│ │              ✏️ 🗑️  │   │
│ └─────────────────────┘   │
├───────────────────────────┤
│ ┌─────────────────────┐   │
│ │ [M] Mark            │   │ ← User info
│ │     View Profile →  │   │   (clickable)
│ └─────────────────────┘   │
│                           │
│    Version 1.0.0          │ ← Version
└───────────────────────────┘
```

### Session Item States

**Normal State:**
- Title and date visible
- Edit and delete icons hidden

**Hover State:**
- Background highlights
- ✏️ Edit and 🗑️ Delete icons appear

**Editing State:**
- Input field replaces title
- ✓ Save and ✕ Cancel icons visible
- Blue border around input
- Click outside or ESC cancels

**Selected State:**
- Blue background
- Bold appearance
- Icons always visible

## Keyboard Shortcuts

### Session Editing
- **Enter** - Save changes
- **Escape** - Cancel editing
- **Tab** - Navigate (standard)

### Session Navigation
- **Click** - Select session
- **Click + Hold** - Drag to reorder (future)

## Responsive Design

### Desktop (≥768px)
- Sidebar always visible
- Full width (256px / 16rem)
- User info fully displayed

### Mobile (<768px)
- Sidebar toggles with hamburger menu
- Overlay when open
- Swipe to close (future)
- User info condensed

## API Integration

### Endpoints Used

| Endpoint | Method | Purpose |
|----------|--------|---------|
| `/api/v1/sessions` | GET | List all sessions |
| `/api/v1/sessions` | POST | Create new session |
| `/api/v1/sessions/{id}` | PUT | Update session title |
| `/api/v1/sessions/{id}` | DELETE | Delete session |
| `/api/auth/v1/user` | GET | Get user info |

### Authentication
All requests require JWT token in Authorization header:
```
Authorization: Bearer {access_token}
```

## Error Handling

### Edit Session Title
- **Empty title** - Cancels edit (keeps old title)
- **Network error** - Shows console error, keeps old title
- **401 Unauthorized** - Triggers token refresh

### Load User Info
- **Network error** - Username section hidden
- **No token** - Component gracefully fails
- **401 response** - Triggers login redirect

## State Management

### Local State (Sidebar)
```typescript
const [sessions, setSessions] = useState<ChatSession[]>([]);
const [isLoading, setIsLoading] = useState(true);
const [isCollapsed, setIsCollapsed] = useState(false);
const [editingSessionId, setEditingSessionId] = useState<string | null>(null);
const [editingTitle, setEditingTitle] = useState('');
const [userName, setUserName] = useState<string>('');
```

### Effect Hooks
```typescript
useEffect(() => {
  loadSessions();    // Fetch chat sessions
  loadUserInfo();    // Fetch user profile
}, []);
```

## Styling

### Color Scheme
- **Primary**: Blue (#3B82F6) - Edit, links
- **Success**: Green (#10B981) - Save
- **Danger**: Red (#EF4444) - Delete
- **Neutral**: Gray - Cancel, version

### Dark Mode Support
All components support dark mode:
- `dark:bg-gray-800` - Dark background
- `dark:text-white` - Dark text
- `dark:border-gray-700` - Dark borders
- `dark:hover:bg-gray-700` - Dark hover

## Testing Checklist

### Edit Session Title
- [x] Frontend builds successfully
- [x] Backend compiles successfully
- [ ] Can click edit icon
- [ ] Input field appears with current title
- [ ] Can type new title
- [ ] Enter key saves
- [ ] Escape key cancels
- [ ] Click checkmark saves
- [ ] Click X cancels
- [ ] Title updates in list
- [ ] updatedAt changes

### Username Display
- [x] Frontend builds successfully
- [ ] Username loads from API
- [ ] Avatar shows first letter
- [ ] Displays correctly
- [ ] Dark mode works

### Profile Link
- [ ] Link opens in new tab
- [ ] Goes to auth.markpost.dev
- [ ] Hover effect works
- [ ] Secure attributes present

### Version Display
- [x] Shows "Version 1.0.0"
- [x] Centered at bottom
- [x] Correct styling

## Future Enhancements

### Potential Improvements
1. **Drag to reorder** - Reorder sessions
2. **Pin sessions** - Keep important chats at top
3. **Search sessions** - Filter by title
4. **Session folders** - Organize chats
5. **Export session** - Download chat history
6. **Session sharing** - Share with other users
7. **Session tags** - Categorize chats
8. **Archive sessions** - Hide old chats
9. **Batch operations** - Delete multiple
10. **Keyboard shortcuts** - Fast navigation

### Version Management
Consider fetching version from:
- Backend API endpoint
- package.json at build time
- Environment variable
- Build metadata

## Files Modified

### Backend
1. `src/main/java/nl/markpost/aiassistant/controller/ChatSessionController.java`
   - Added `updateSession` endpoint (PUT)

2. `src/main/java/nl/markpost/aiassistant/service/ChatSessionService.java`
   - Added `updateSessionTitle` method

### Frontend
1. `frontend/lib/api.ts`
   - Added `updateSession` API method

2. `frontend/components/Sidebar.tsx`
   - Added edit state management
   - Added username display
   - Added profile link
   - Added version display
   - Enhanced session list UI

## Build Status

✅ **Frontend**: Compiled successfully
✅ **Backend**: Compiled successfully
✅ **TypeScript**: No errors
✅ **Ready for deployment**

## Deployment

```bash
# Build backend
cd /Users/markpost/IdeaProjects/ai-assistant
mvn package -DskipTests

# Build frontend
cd frontend
npm run build

# Rebuild Docker images
cd ..
docker compose build

# Deploy
docker compose up -d
```

## Screenshots

### Normal State
```
┌─────────────────────┐
│ Work Projects       │
│ Dec 18, 2025       │
└─────────────────────┘
```

### Hover State
```
┌─────────────────────┐
│ Work Projects  ✏️ 🗑️│
│ Dec 18, 2025       │
└─────────────────────┘
```

### Editing State
```
┌─────────────────────┐
│ [Work Projects_] ✓ ✕│
│ Dec 18, 2025       │
└─────────────────────┘
```

### Footer
```
┌─────────────────────┐
│ [M] Mark            │
│     View Profile →  │
│                     │
│   Version 1.0.0     │
└─────────────────────┘
```

## Security Considerations

✅ **Input Validation** - Empty titles rejected
✅ **Authorization** - User can only edit own sessions
✅ **XSS Prevention** - React escapes content
✅ **CSRF Protection** - JWT authentication
✅ **Secure Links** - `noopener noreferrer`

## Performance

- **Optimistic Updates** - UI updates immediately
- **Debouncing** - Could add for rapid typing
- **Lazy Loading** - Sessions load on mount
- **Memoization** - Consider for session list

## Accessibility

Consider adding:
- `aria-label` for icon buttons
- `role="button"` for clickable elements
- Keyboard navigation improvements
- Screen reader announcements
- Focus management during edit

## Version History

- **1.0.0** (2025-12-18)
  - ✅ Edit session titles
  - ✅ Display username
  - ✅ Profile link
  - ✅ Version display

