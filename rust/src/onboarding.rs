use std::io;

macro_rules! parse_input {
    ($x:expr, $t:ident) => {
        $x.trim().parse::<$t>().unwrap()
    };
}

type LineReader = dyn FnMut() -> String;

fn read_line() -> String {
    let mut input_line = String::new();
    io::stdin().read_line(&mut input_line).unwrap();
    input_line
}

fn read_trimmed_line(read_line: &mut LineReader) -> String {
    let x = read_line();
    x.trim().to_string()
}

struct Enemy {
    name: String,
    dist: i32,
}

fn read_enemy(read_line: &mut LineReader) -> Enemy {
    Enemy {
        name: read_trimmed_line(read_line),
        dist: parse_input!(read_trimmed_line(read_line), i32),
    }
}

#[allow(dead_code)]
pub fn main() {
    loop {
        let enemy_1 = read_enemy(&mut read_line);
        let enemy_2 = read_enemy(&mut read_line);

        if enemy_1.dist < enemy_2.dist {
            println!("{}", enemy_1.name);
        } else {
            println!("{}", enemy_2.name);
        }
    }
}

#[cfg(test)]
mod test {
    use onboarding::read_enemy;

    fn enemy_string(enemy: &'static str) -> impl FnMut() -> String {
        let split: Vec<&str> = enemy.split(":").collect();
        let mut i = 0;
        move || {
            let result = split[i].to_string();
            i += 1;
            result
        }
    }

    #[test]
    fn test() {
        let enemy = read_enemy(&mut enemy_string("enemy1:10"));

        assert_eq!(enemy.name, "enemy1");
        assert_eq!(enemy.dist, 10);
    }
}
