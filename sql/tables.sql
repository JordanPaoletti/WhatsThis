CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  username VARCHAR(50) NOT NULL,
  post_points INTEGER DEFAULT 0,
  answer_points INTEGER DEFAULT 0,
  admin BOOLEAN DEFAULT FALSE
);

CREATE TABLE classroom (
  id SERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL
);

CREATE TABLE classroom_users (
  class_id INTEGER NOT NULL,
  user_id INTEGER NOT NULL,
  FOREIGN KEY (class_id) REFERENCES classroom (id),
  FOREIGN KEY (user_id) REFERENCES users (id),
  PRIMARY KEY (class_id, user_id)
);

CREATE TABLE notification_types (
  id SERIAL PRIMARY KEY,
  name VARCHAR(25) NOT NULL
);

CREATE TABLE user_notifications (
  id SERIAL PRIMARY KEY,
  type_id INTEGER NOT NULL,
  read BOOLEAN DEFAULT FALSE,
  time TIMESTAMP DEFAULT NOW(),
  cause_id INTEGER,
  link_id INTEGER
);

CREATE TABLE asks (
  id SERIAL PRIMARY KEY,
  img_link VARCHAR(256) NOT NULL,
  description VARCHAR(256) DEFAULT '',
  poster_id INTEGER NOT NULL,
  class_id INTEGER NOT NULL,
  time TIMESTAMP DEFAULT NOW(),
  answered BOOLEAN DEFAULT FALSE,
  points INTEGER DEFAULT 0,
  FOREIGN KEY (poster_id) REFERENCES users (id),
  FOREIGN KEY (class_id) REFERENCES classroom (id)
);

CREATE TABLE followed_asks (
  user_id INTEGER NOT NULL,
  ask_id INTEGER NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users (id),
  FOREIGN KEY (ask_id) REFERENCES asks (id),
  PRIMARY KEY (user_id, ask_id)
);

CREATE TABLE answers (
  answer TEXT NOT NULL,
  poster_id INTEGER NOT NULL,
  ask_id INTEGER NOT NULL,
  points INTEGER DEFAULT 0,
  accepted BOOLEAN DEFAULT FALSE,
  FOREIGN KEY (poster_id) REFERENCES users (id),
  FOREIGN KEY (ask_id) REFERENCES asks (id),
  PRIMARY KEY (ask_id, poster_id)
);

CREATE TABLE answer_replies (
  id SERIAL PRIMARY KEY,
  message TEXT NOT NULL,
  poster_id INTEGER NOT NULL,
  parent_id INTEGER NULL,
  FOREIGN KEY (poster_id) REFERENCES users (id),
  FOREIGN KEY (parent_id) REFERENCES answer_replies (id)
)
