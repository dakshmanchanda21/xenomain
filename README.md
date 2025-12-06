Assumptions - 
Each Shopify store is mapped to a tenant (e.g. tenant_id = 2 in DB), and one user/email logs in for that tenant.

We ingest customers, orders and custom events from a single test store, but the schema supports multiple stores (multi-tenant) via tenant_id.

Currency is assumed to be INR and we display metrics using the same.

Shopify data is treated as the source of truth; we only append new records, we don’t modify Shopify state.

For demo purposes, if some metrics (funnel events, last-7-days stats) are missing from the API, the UI computes reasonable defaults from existing data.


High Level Architechture - 
Frontend (React + Vite)

Authenticates the user with email / Auth0.

Lets user connect a Shopify store (domain + access token).

Shows the Shopify Insights dashboard: summary cards, orders-by-date chart, revenue histogram, engagement funnel, and top customers.

Backend (Spring Boot)

REST APIs under /api/**.

Handles tenant onboarding, Shopify connection and ingestion.

Computes aggregated metrics for the dashboard (/api/metrics/dashboard).

Uses JPA/Hibernate (or JDBC) to persist into a relational DB.

Database (MySQL/Postgres)

Core tables (with tenant_id):

tenant – tenants / merchants.

shopify_store – store connection details.

customer – customer profiles.

orders – one row per order (date, total_price, currency, customer_id).

order_line_items – product breakdown.

shop_event – custom events like cart abandoned / checkout started.



Data flow - 
User logs into frontend → token → stored in local storage.

User connects Shopify store → frontend sends domain + access token to backend → backend validates & saves to shopify_store.

Sync job / manual sync → backend calls Shopify Admin API → normalizes JSON into customer, orders, order_line_items, shop_event.

Dashboard page calls /api/metrics/dashboard?tenantId=2&from=YYYY-MM-DD&to=YYYY-MM-DD.

Backend runs SQL/JPQL aggregations and returns:

totals (customers, orders, revenue, AOV),

ordersByDate array,

funnel counts,

top revenue days,

top customers by spend.

React renders these into summary cards, line chart, histogram and pie chart.



APIs and data models used - 

POST /api/tenants/connect-shopify

Body: { "tenantId": 2, "shopifyDomain": "my-store.myshopify.com", "accessToken": "shp_***" }

Purpose: store Shopify credentials for a tenant.

POST /api/ingestion/sync

Body: { "tenantId": 2 }

Purpose: pull latest customers/orders/events from Shopify and upsert into DB.

GET /api/metrics/dashboard

Query params: tenantId, from, to

Response (shape used on frontend):
             {
  "totalCustomers": 6,
  "totalOrders": 12,
  "totalRevenue": 8388,
  "avgOrderValue": 699,
  "last7DaysOrders": 2,
  "last7DaysRevenue": 5394,
  "orderVelocity30Days": 2,
  "ordersByDate": [
    { "date": "2025-12-03", "orders": 4, "revenue": 2994 },
    { "date": "2025-12-05", "orders": 8, "revenue": 5394 }
  ],
  "cartAbandoned": 4,
  "checkoutStarted": 7,
  "checkoutCompleted": 5,
  "topRevenueDays": [
    { "date": "2025-12-05", "revenue": 5394 },
    { "date": "2025-12-03", "revenue": 2994 }
  ],
  "topCustomersBySpend": [
    { "customerId": 1, "name": "Daksh Demo 1", "spend": 3999 },
    ...
  ]
}
(Optional for docs) GET /api/customers, GET /api/orders – used during dev/testing to verify ingestion.

Core Data Models

Customer: id, tenant_id, email, first_name, last_name, phone, shopify_customer_id.

Order: id, tenant_id, customer_id, order_date, shopify_order_id, currency, total_price.

ShopEvent: id, tenant_id, customer_id, order_id, event_type (CART_ABANDONED, CHECKOUT_STARTED, CHECKOUT_COMPLETED), created_at.

Tenant: id, name, email, created_at.

ShopifyStore: id, tenant_id, domain, access_token, created_at.


Limitations and next-steps 

Harden multi-tenancy:

Enforce tenant isolation at the DB layer (schema per tenant or row-level security).

Add proper RBAC for multiple users per tenant.

Make sync fully automatic:

Replace manual sync with a combination of scheduled jobs and Shopify webhooks.

Track last synced timestamp to only ingest incremental changes.

Improve performance and scalability:

Add indices on tenant_id, order_date, customer_id.

Move heavy aggregations to materialized views or nightly batch jobs.

Introduce Redis caching for common dashboard ranges.

Improve security:

Store Shopify access tokens encrypted / in a secrets manager.

Enforce HTTPS, CSRF protection and stricter CORS.

Add audit logging for ingestion and admin actions.

UX improvements:

Add filters by channel / product / device.

Add drill-down pages from top customers and top revenue days.

CI/CD & monitoring:

GitHub Actions pipeline to run tests and deploy to Render/Vercel.

Metrics and logs (Grafana/Prometheus or hosted equivalent) for error tracking and latency.
