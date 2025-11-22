INSERT INTO issue (
    id,
    summary,
    description,
    office,
    status,
    created_by,
    date_created,
    date_modified
) VALUES
-- 1
(
    gen_random_uuid(),
    'Meeting room lights flickering',
    'The lights in the main meeting room randomly flicker during presentations.',
    'Vilnius Office',
    'OPEN',
    NULL,
    NOW(),
    NOW()
),
-- 2
(
    gen_random_uuid(),
    'Printer not responding',
    'The 3rd floor printer frequently loses connection and gets stuck in queue.',
    'Kaunas Office',
    'OPEN',
    NULL,
    NOW(),
    NOW()
),
-- 3
(
    gen_random_uuid(),
    'Slow Wi-Fi connection',
    'Wi-Fi on the 5th floor is extremely unstable during peak hours.',
    'Vilnius Office',
    'IN_PROGRESS',
    NULL,
    NOW(),
    NOW()
),
-- 4
(
    gen_random_uuid(),
    'Kitchen fridge not cooling',
    'Food is spoiling because the fridge temperature is too high.',
    'Vilnius Office',
    'OPEN',
    NULL,
    NOW(),
    NOW()
),
-- 5
(
    gen_random_uuid(),
    'Air conditioning too loud',
    'The AC unit produces a loud buzzing sound in the developer area.',
    'Vilnius Office',
    'RESOLVED',
    NULL,
    NOW(),
    NOW()
),
-- 6
(
    gen_random_uuid(),
    'Broken monitor in shared workspace',
    'One of the shared monitors displays flickering horizontal artifacts.',
    'Kaunas Office',
    'OPEN',
    NULL,
    NOW(),
    NOW()
),
-- 7
(
    gen_random_uuid(),
    'Coffee machine running out of beans',
    'The coffee machine does not notify when beans run low, causing errors.',
    'Vilnius Office',
    'OPEN',
    NULL,
    NOW(),
    NOW()
),
-- 8
(
    gen_random_uuid(),
    'Overheating in server closet',
    'Temperature in the server closet reaches unsafe levels after 17:00.',
    'Kaunas Office',
    'IN_PROGRESS',
    NULL,
    NOW(),
    NOW()
),
-- 9
(
    gen_random_uuid(),
    'Door access card malfunction',
    'Employee cards randomly stop working on the 4th floor entrance.',
    'Vilnius Office',
    'OPEN',
    NULL,
    NOW(),
    NOW()
),
-- 10
(
    gen_random_uuid(),
    'Projector remote missing',
    'The conference room projector cannot be used because the remote is gone.',
    'Kaunas Office',
    'CLOSED',
    NULL,
    NOW(),
    NOW()
),

-- 11
(
    gen_random_uuid(),
    'Elevator intermittently stops',
    'The elevator sometimes stops between floors and restarts unexpectedly during peak hours.',
    'Gdansk Office',
    'OPEN',
    NULL,
    NOW(),
    NOW()
),
-- 12
(
    gen_random_uuid(),
    'Broken AC in meeting room',
    'The AC in the 3rd floor meeting room fails to cool effectively.',
    'Vilnius Office',
    'OPEN',
    NULL,
    NOW(),
    NOW()
),
-- 13
(
    gen_random_uuid(),
    'Kitchen fridge smells bad',
    'Despite cleaning, the fridge still emits a strong odor.',
    'Vilnius Office',
    'CLOSED',
    NULL,
    NOW(),
    NOW()
),
-- 14
(
    gen_random_uuid(),
    'Monitor flickering at high brightness',
    'Several monitors flicker when brightness exceeds 70%.',
    'Kaunas Office',
    'OPEN',
    NULL,
    NOW(),
    NOW()
),
-- 15
(
    gen_random_uuid(),
    'Coffee machine leaking',
    'The coffee machine in the 2nd floor kitchen leaks water from the tray.',
    'Gdansk Office',
    'PLANNED',
    NULL,
    NOW(),
    NOW()
),
-- 16
(
    gen_random_uuid(),
    'Access card failure in parking garage',
    'Access cards do not reliably open the parking garage gate.',
    'Vilnius Office',
    'RESOLVED',
    NULL,
    NOW(),
    NOW()
),
-- 17
(
    gen_random_uuid(),
    'Window draft in Office 214',
    'Cold air enters through window edges, making it uncomfortable.',
    'Vilnius Office',
    'OPEN',
    NULL,
    NOW(),
    NOW()
),
-- 18
(
    gen_random_uuid(),
    'Wi-Fi drops during presentations',
    'Wi-Fi disconnects when switching presenters in the meeting room.',
    'Vilnius Office',
    'RESOLVED',
    NULL,
    NOW(),
    NOW()
),
-- 19
(
    gen_random_uuid(),
    'Standing desk stuck',
    'The adjustable standing desk will not move up or down.',
    'Gdansk Office',
    'OPEN',
    NULL,
    NOW(),
    NOW()
),
-- 20
(
    gen_random_uuid(),
    'Unstable elevator vibrations',
    'Elevator shakes when moving between floors, concerning employees.',
    'Kaunas Office',
    'PLANNED',
    NULL,
    NOW(),
    NOW()
),
-- 21
(
    gen_random_uuid(),
    'Meeting rooms oftenly booked',
    'Sometimes it''s hard to find a free meeting room for discussions or client calls.',
    'Kaunas Office',
    'OPEN',
    NULL,
    NOW(),
    NOW()
),
-- 22
      (
    gen_random_uuid(),
    'Slow Wi-Fi on the 2nd floor',
    'Internet connection drops occasionally, slowing work.',
    'Vilnius Office',
    'IN_PROGRESS',
    NULL,
    NOW(),
    NOW()
    ),
-- 23
(
    gen_random_uuid(),
    '3rd floor printer not discoverable',
    'Printer in the third-floor hallway doesn''t appear on Wi-Fi or Bluetooth sometimes.',
    'Gdansk Office',
    'CLOSED',
    NULL,
    NOW(),
    NOW()
),
-- 24
(
    gen_random_uuid(),
    'Projector and AV setup unreliable',
    'Printer in the third-floor hallway doesn''t appear on Wi-Fi or Bluetooth sometimes.',
    'Gdansk Office',
    'CLOSED',
    NULL,
    NOW(),
    NOW()
),
-- 25
(
    gen_random_uuid(),
    'HDMI Cable does not work in the meeting room',
    'When connecting HDMI cable to the computer, it doesn''t detect signal.',
    'Vilnius Office',
    'OPEN',
    NULL,
    NOW(),
    NOW()
),




