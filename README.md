# voNation web (demo, read-only)

A public page that shows events, open campaigns and announcements from your Neon database.
It only reads data. There is no login, and nothing here can change the database.

## 1. Make a read-only database user (do this first)

In the Neon SQL Editor (run as your main Neon user), change the password in the first line and run:

```sql
CREATE ROLE web_readonly LOGIN PASSWORD 'CHANGE-ME-to-a-long-random-password';

-- Progress totals without exposing donor names
CREATE OR REPLACE VIEW campaign_progress AS
  SELECT campaign_id, SUM(amount) AS raised
  FROM donations
  WHERE campaign_id IS NOT NULL AND status NOT IN ('Pending', 'Cancelled', 'Canceled')
  GROUP BY campaign_id;

GRANT USAGE ON SCHEMA public TO web_readonly;
GRANT SELECT ON events, campaigns, announcements, campaign_progress TO web_readonly;
```

If `CREATE ROLE` is refused, create the role in the Neon console instead (Roles > New role), then run the other lines.

Your connection string is the same as the main one, but with `web_readonly` and its password:

```
postgresql://web_readonly:PASSWORD@YOUR-NEON-HOST/neondb?sslmode=require
```

## 2. Test locally (optional)

```
npm install
copy .env.example .env.local     (then put your real string in .env.local)
npm run dev
```

Open http://localhost:3000

## 3. Put it on GitHub

Make a NEW Private repo (separate from the Java project), then in this folder:

```
git init
git add .
git ls-files
git commit -m "Initial commit"
git branch -M main
git remote add origin https://github.com/YOUR-NAME/vonation-web.git
git push -u origin main
```

`git ls-files` must not show `.env`, `.env.local` or `node_modules`.

## 4. Deploy on Vercel

1. vercel.com > Add New > Project > Import the repo.
2. Before pressing Deploy, open Environment Variables and add `DATABASE_URL` with the read-only string.
3. Deploy.

Photos uploaded in the desktop app are saved on your computer, not in Neon, so they do not show on the web page.
