CREATE TABLE IF NOT EXISTS public.users_info
(
    id                  SERIAL PRIMARY KEY,
    user_name           CHARACTER VARYING(255) NOT NULL,
    email               CHARACTER VARYING(255) NOT NULL UNIQUE,
    user_age            INTEGER NOT NULL,
    user_weight         NUMERIC NOT NULL,
    user_height         NUMERIC NOT NULL,
    purpose             CHARACTER VARYING(30) NOT NULL,
    calories_day_norm   INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS public.dishes_info
(
    id          SERIAL PRIMARY KEY,
    dish_name   CHARACTER VARYING(255) NOT NULL UNIQUE,
    calories    INTEGER NOT NULL,
    pfc       CHARACTER VARYING(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS public.meals_info
(
    id             SERIAL PRIMARY KEY,
    meal_date      DATE NOT NULL,
    meal_name      CHARACTER VARYING(255) NOT NULL,
    user_id        BIGINT REFERENCES users_info(id) ON DELETE CASCADE
);
