use std::io;

macro_rules! parse_input {
    ($x:expr, $t:ident) => {
        $x.trim().parse::<$t>().unwrap()
    };
}

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
fn main() {
    let mut input_line = String::new();
    io::stdin().read_line(&mut input_line).unwrap();
    let n = parse_input!(input_line, i32);
    for i in 0..n as usize {
        let mut input_line = String::new();
        io::stdin().read_line(&mut input_line).unwrap();
        let inputs = input_line.split(" ").collect::<Vec<_>>();
        let operation = inputs[0].trim().to_string();
        let arg_1 = inputs[1].trim().to_string();
        let arg_2 = inputs[2].trim().to_string();
    }
    for i in 0..n as usize {
        // Write an answer using println!("message...");
        // To debug: eprintln!("Debug message...");

        println!("1");
    }
}
