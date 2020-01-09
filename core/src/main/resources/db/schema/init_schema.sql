CREATE USER brainweb WITH password 'brainweb';

CREATE SCHEMA interview_service;
ALTER USER brainweb SET search_path = 'interview_service, public';

GRANT USAGE, CREATE ON SCHEMA interview_service TO brainweb;
GRANT ALL ON ALL TABLES IN SCHEMA interview_service TO brainweb;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA interview_service TO brainweb;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA interview_service TO brainweb;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA interview_service TO brainweb;

SET SCHEMA 'interview_service';
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
GRANT EXECUTE ON FUNCTION uuid_generate_v4() TO brainweb;
