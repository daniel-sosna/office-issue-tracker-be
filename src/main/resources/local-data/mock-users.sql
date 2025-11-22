INSERT INTO users (id, name, email, role, image_url)
SELECT gen_random_uuid(), u.name, u.email, u.role, u.image_url
FROM (
         VALUES
             ('John Smith',       'john.smith@example.com',       'USER', 'https://lh3.googleusercontent.com/a/ACg8ocLJHS847shf9d8sdf87sdf79sd7fs9=s96-c'),
             ('Emily Johnson',    'emily.johnson@example.com',    'USER', 'https://lh3.googleusercontent.com/a/ACg8ocJS8df7sd78fsdf7s8df7sd78f7sdf=s96-c'),
             ('Michael Brown',    'michael.brown@example.com',    'USER', 'https://lh3.googleusercontent.com/a/ACg8ocKK9d8f7sd7f89sdf7sdf7sdf7s89f=s96-c'),
             ('Sarah Davis',      'sarah.davis@example.com',      'USER', 'https://lh3.googleusercontent.com/a/ACg8ocPLmdf78sd7f89sd8f7s9df8s7df9=s96-c'),
             ('David Wilson',     'david.wilson@example.com',     'USER', 'https://lh3.googleusercontent.com/a/ACg8ocTTd7f89sd7f8sd7f8s7df8s7d8f7=s96-c'),
             ('Olivia Martin',    'olivia.martin@example.com',    'USER', 'https://lh3.googleusercontent.com/a/ACg8ocQQdf78sdf78sdf78sdf78sd8f7sd=s96-c'),
             ('James Anderson',   'james.anderson@example.com',   'USER', 'https://lh3.googleusercontent.com/a/ACg8ocFF7sd87fsd7f8sd7f8sd7fsd8f7s=s96-c'),
             ('Sophia Thomas',    'sophia.thomas@example.com',    'USER', 'https://lh3.googleusercontent.com/a/ACg8ocWQ8sdf7sdf7sdf7sd87f7s8d7f8s=s96-c'),
             ('Daniel Harris',    'daniel.harris@example.com',    'USER', 'https://lh3.googleusercontent.com/a/ACg8ocRR78sd7f89sd7f8sd7fsd8f7s9df=s96-c'),
             ('Emma Lee',         'emma.lee@example.com',         'USER', 'https://lh3.googleusercontent.com/a/ACg8ocJJ7df89sd7f8s7d8f7sd8f7s8df=s96-c')
     ) AS u(name, email, role, image_url)
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE users.email = u.email
);
