CREATE OR REPLACE FUNCTION user_vote_ask(user_id_var INTEGER, ask_id_var INTEGER) RETURNS VOID
AS $$
  BEGIN
  IF NOT EXISTS (SELECT * FROM followed_asks fa
                  WHERE fa.user_id = user_id_var AND fa.ask_id = ask_id_var) THEN
    INSERT INTO followed_asks (user_id, ask_id) VALUES (user_id_var, ask_id_var);
    UPDATE asks SET points = points + 1 WHERE id = ask_id_var;
    END IF;
  END
 $$ LANGUAGE plpgsql;

