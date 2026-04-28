insert into app_user (first_name, last_name, email, birth_date, role, password) values
                                                                                    ('Admin', 'User', 'admin@trailhub.com', '1990-01-01', 'ADMIN', '{noop}admin123'),
                                                                                    ('John', 'Doe', 'john@trailhub.com', '1995-03-10', 'USER', '{noop}user123'),
                                                                                    ('Michael', 'Schofield', 'michael@trailhub.com', '1992-07-22', 'USER', '{noop}user123');

insert into race (name, distance) values
                                      ('Spring Trail 5K', 'FIVE_K'),
                                      ('Mountain Challenge 10K', 'TEN_K'),
                                      ('Forest Half', 'HALF_MARATHON'),
                                      ('City Marathon', 'MARATHON');
