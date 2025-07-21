set -e

echo "Waiting for DB..."
until pg_isready -h postgres-write -p 5432 -U "$POSTGRES_USER"; do
  sleep 2
done

echo "Running init script..."
export PGPASSWORD="$POSTGRES_PASSWORD"
psql -h postgres-write -U "$POSTGRES_USER" -d "$POSTGRES_DB" -f /init/create-tables.sql