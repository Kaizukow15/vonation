import { getSql } from "../lib/db";

// Re-read the database at most once a minute.
export const revalidate = 60;

async function safe(fn, fallback) {
  try {
    return await fn();
  } catch (e) {
    console.error(e.message);
    return fallback;
  }
}

async function loadData() {
  const sql = getSql();

  const events = await safe(
    () => sql`
      SELECT id, name, event_date::text AS date, time_slot, location, category, status
      FROM events
      WHERE status NOT IN ('Cancelled', 'Canceled')
      ORDER BY event_date`,
    null
  );

  const campaigns = await safe(
    () => sql`
      SELECT id, name, description, goal_amount::float8 AS goal, campaign_type AS type,
             items_needed, category, walkin_location
      FROM campaigns
      WHERE status = 'Active'
      ORDER BY id DESC`,
    null
  );

  // Optional: needs the campaign_progress view from README.md. Without it, progress bars are hidden.
  const progress = await safe(
    () => sql`SELECT campaign_id, raised::float8 AS raised FROM campaign_progress`,
    []
  );

  const announcements = await safe(
    () => sql`
      SELECT id, title, body, tag, created_at::date::text AS date
      FROM announcements
      ORDER BY id DESC
      LIMIT 6`,
    null
  );

  return { events, campaigns, progress, announcements };
}

function day(d, opts) {
  return new Date(`${d}T00:00:00Z`).toLocaleDateString("en-PH", { timeZone: "UTC", ...opts });
}

const peso = (n) => "₱" + Number(n).toLocaleString("en-PH", { maximumFractionDigits: 0 });

export default async function Home() {
  let data;
  try {
    data = await loadData();
  } catch (e) {
    return (
      <main className="wrap">
        <h1 className="logo">voNation</h1>
        <p className="empty">The page could not connect to the database. Check DATABASE_URL in Vercel.</p>
      </main>
    );
  }

  const { events, campaigns, progress, announcements } = data;
  const today = new Date().toISOString().slice(0, 10);
  const upcoming = (events || []).filter((e) => e.date >= today);
  const past = (events || []).filter((e) => e.date < today).reverse().slice(0, 4);
  const next = upcoming[0];
  const raisedBy = new Map(progress.map((p) => [p.campaign_id, p.raised]));
  const hasProgress = progress.length > 0;

  return (
    <>
      <header className="top wrap">
        <span className="logo">voNation</span>
        <nav>
          <a href="#events">Events</a>
          <a href="#campaigns">Campaigns</a>
          <a href="#news">Announcements</a>
        </nav>
      </header>

      <section className="hero">
        <div className="wrap hero-grid">
          {next ? (
            <>
              <div className="when" aria-hidden="true">
                <span className="when-day">{day(next.date, { day: "numeric" })}</span>
                <span className="when-month">{day(next.date, { month: "long" })}</span>
              </div>
              <div>
                <p className="hero-lead">Next volunteer event</p>
                <h1>{next.name}</h1>
                <p className="hero-meta">
                  {[next.time_slot, next.location].filter(Boolean).join(", ")}
                </p>
                <a className="btn" href="#events">See all events</a>
              </div>
            </>
          ) : (
            <div>
              <h1>Volunteer, donate, and show up for your community.</h1>
              <a className="btn" href="#campaigns">See open campaigns</a>
            </div>
          )}
        </div>
      </section>

      <main className="wrap">
        <section id="events">
          <h2>Upcoming events</h2>
          {events === null ? (
            <p className="empty">Events could not be loaded right now.</p>
          ) : upcoming.length === 0 ? (
            <p className="empty">No upcoming events yet. Check back soon.</p>
          ) : (
            <ul className="events">
              {upcoming.map((e) => (
                <li key={e.id}>
                  <div className="date">
                    <b>{day(e.date, { day: "numeric" })}</b>
                    <span>{day(e.date, { month: "short" })}</span>
                  </div>
                  <div>
                    <h3>{e.name}</h3>
                    <p>{[e.time_slot, e.location].filter(Boolean).join(", ")}</p>
                  </div>
                  {e.category && <span className="chip">{e.category}</span>}
                </li>
              ))}
            </ul>
          )}
          {past.length > 0 && (
            <p className="past">
              Recently held: {past.map((e) => `${e.name} (${day(e.date, { month: "short", day: "numeric" })})`).join("; ")}
            </p>
          )}
        </section>

        <section id="campaigns">
          <h2>Open campaigns</h2>
          {campaigns === null ? (
            <p className="empty">Campaigns could not be loaded right now.</p>
          ) : campaigns.length === 0 ? (
            <p className="empty">No campaigns are open at the moment.</p>
          ) : (
            <div className="campaigns">
              {campaigns.map((c) => {
                const raised = raisedBy.get(c.id) || 0;
                const pct = c.goal > 0 ? Math.min(100, Math.round((raised / c.goal) * 100)) : 0;
                const cash = c.type === "Cash";
                return (
                  <article key={c.id}>
                    <div className="c-head">
                      <h3>{c.name}</h3>
                      <span className="chip">{c.category}</span>
                    </div>
                    {c.description && <p>{c.description}</p>}
                    {cash && c.goal > 0 && hasProgress && (
                      <div className="bar-wrap">
                        <div className="bar" role="progressbar" aria-valuenow={pct} aria-valuemin="0" aria-valuemax="100">
                          <i style={{ width: `${pct}%` }} />
                        </div>
                        <p className="bar-text">
                          {peso(raised)} raised of {peso(c.goal)} ({pct}%)
                        </p>
                      </div>
                    )}
                    {!cash && c.items_needed && <p className="needs">Needed: {c.items_needed}</p>}
                    {c.walkin_location && <p className="where">Drop-off: {c.walkin_location}</p>}
                  </article>
                );
              })}
            </div>
          )}
        </section>

        <section id="news">
          <h2>Community announcements</h2>
          {announcements === null ? (
            <p className="empty">Announcements could not be loaded right now.</p>
          ) : announcements.length === 0 ? (
            <p className="empty">No announcements yet.</p>
          ) : (
            <ul className="news">
              {announcements.map((a) => (
                <li key={a.id}>
                  <time dateTime={a.date}>{day(a.date, { month: "short", day: "numeric", year: "numeric" })}</time>
                  <div>
                    <h3>{a.title}</h3>
                    <p>{a.body}</p>
                  </div>
                  {a.tag && <span className="chip">{a.tag}</span>}
                </li>
              ))}
            </ul>
          )}
        </section>
      </main>

      <footer className="wrap">
        Demo web view of the voNation desktop app. Information is read from the app's database and refreshes about once a minute.
      </footer>
    </>
  );
}
