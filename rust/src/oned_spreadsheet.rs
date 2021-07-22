use std::{cell, io};

fn read_line() -> String {
    let mut input_line = String::new();
    io::stdin()
        .read_line(&mut input_line)
        .expect("cannot read stdin");
    input_line.trim().to_string()
}

enum Value {
    Value(i32),
    Reference(u8),
    None(),
}

impl std::str::FromStr for Value {
    type Err = ();

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        if s.starts_with("$") {
            Ok(Value::Reference(
                s[1..].parse().expect(&format!("cannot parse ref {}", s)),
            )) //TODO handle error
        } else if s == "_" {
            Ok(Value::None())
        } else {
            Ok(Value::Value(
                s.parse().expect(&format!("cannot parse value {}", s)),
            )) //TODO handle error
        }
    }
}

impl Value {
    fn calc(&self, cells: &Vec<Cell>) -> i32 {
        match self {
            Value::Value(value) => value.clone(),
            Value::Reference(reference) => {
                let cell = cells.get(usize::from(*reference)).unwrap();
                cell.calc(cells)
            }
            Value::None() => panic!("none as no value"),
        }
    }
}

struct Cell {
    operation: Operation,
    cached_value: cell::Cell<Option<i32>>,
}

impl Cell {
    fn calc(&self, cells: &Vec<Cell>) -> i32 {
        if let Some(value) = self.cached_value.get() {
            value
        } else {
            let value = self.operation.calc(cells);
            self.cached_value.set(Some(value));
            value
        }
    }
}

impl From<Operation> for Cell {
    fn from(operation: Operation) -> Self {
        Cell {
            cached_value: cell::Cell::from(Option::None),
            operation,
        }
    }
}

enum Operation {
    Value(Value),
    Add(Value, Value),
    Sub(Value, Value),
    Mult(Value, Value),
}

impl Operation {
    fn calc(&self, cells: &Vec<Cell>) -> i32 {
        match self {
            Operation::Value(v) => v.calc(cells),
            Operation::Add(v1, v2) => v1.calc(cells) + v2.calc(cells),
            Operation::Sub(v1, v2) => v1.calc(cells) - v2.calc(cells),
            Operation::Mult(v1, v2) => v1.calc(cells) * v2.calc(cells),
        }
    }
}

impl std::str::FromStr for Operation {
    type Err = ();

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let splitted: Vec<&str> = s.split(" ").collect();
        let read = |i: usize| splitted[i].parse::<Value>().expect("cannot parse value"); //TODO handle error

        match splitted[0] {
            "VALUE" => Ok(Operation::Value(read(1))),
            "ADD" => Ok(Operation::Add(read(1), read(2))),
            "SUB" => Ok(Operation::Sub(read(1), read(2))),
            "MULT" => Ok(Operation::Mult(read(1), read(2))),
            _ => Err(()),
        }
    }
}

#[allow(unused)]
fn main() {
    run(read_line, |s| println!("{}", s))
}

fn run(mut read_line: impl FnMut() -> String, mut line_printer: impl FnMut(&String) -> ()) {
    let cells: Vec<Cell> = (1..=read_line().parse().expect("cannot parse nb line"))
        .map(|_| {
            read_line()
                .parse::<Operation>()
                .expect("cannot parse operation")
                .into()
        })
        .collect();

    cells
        .iter()
        .for_each(|cell| line_printer(&cell.calc(&cells).to_string()));
}

#[cfg(test)]
mod test {
    use oned_spreadsheet::run;

    fn lines_reader(lines: &'static str) -> impl FnMut() -> String {
        let mut i = 0;
        let splitted: Vec<&str> = lines.split('\n').collect();
        move || {
            let line = splitted[i].to_string();
            i += 1;
            line
        }
    }

    fn input_match_output(input: &'static str, expected_output: Vec<&'static str>) {
        let mut printed_lines = vec![];
        run(lines_reader(input), |str: &String| {
            printed_lines.push(str.clone());
        });

        assert_eq!(printed_lines, expected_output)
    }

    #[test]
    fn value_1_print_1() {
        input_match_output(
            "1
VALUE 1",
            vec!["1"],
        )
    }

    #[test]
    fn sample_is_ok() {
        input_match_output(
            "2
VALUE 3 _
ADD $0 4",
            vec!["3", "7"],
        )
    }

    #[test]
    fn add_1_1_print_2() {
        input_match_output(
            "1
ADD 1 1",
            vec!["2"],
        )
    }
}
