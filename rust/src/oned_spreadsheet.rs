use std::error::Error;
use std::fmt::{Display, Formatter};
use std::{cell, io};

fn read_line() -> String {
    let mut input_line = String::new();
    io::stdin()
        .read_line(&mut input_line)
        .expect("cannot read stdin");
    input_line.trim().to_string()
}

enum Arg {
    Litteral(i32),
    Reference(u8),
    None(),
}

#[derive(Debug)]
enum ParseArgError {
    CannotParseLiteral { cause: Box<dyn Error> },
    CannotParseRef { cause: Box<dyn Error> },
}

impl Display for ParseArgError {
    fn fmt(&self, f: &mut Formatter) -> std::fmt::Result {
        write!(f, "cannot parse argument")
    }
}

impl Error for ParseArgError {
    fn source(&self) -> Option<&(dyn Error + 'static)> {
        match self {
            ParseArgError::CannotParseLiteral { cause } => Some(&**cause),
            ParseArgError::CannotParseRef { cause } => Some(&**cause),
        }
    }
}

impl std::str::FromStr for Arg {
    type Err = ParseArgError;

    fn from_str(s: &str) -> Result<Self, ParseArgError> {
        if s.starts_with("$") {
            Ok(Arg::Reference(s[1..].parse().map_err(|err| {
                ParseArgError::CannotParseRef {
                    cause: Box::new(err),
                }
            })?))
        } else if s == "_" {
            Ok(Arg::None())
        } else {
            Ok(Arg::Litteral(s.parse().map_err(|err| {
                ParseArgError::CannotParseLiteral {
                    cause: Box::new(err),
                }
            })?))
        }
    }
}

impl Arg {
    fn calc(&self, cells: &Vec<Cell>) -> i32 {
        match self {
            Arg::Litteral(value) => value.clone(),
            Arg::Reference(reference) => {
                let cell = cells.get(usize::from(*reference)).unwrap();
                cell.calc(cells)
            }
            Arg::None() => panic!("none as no value"),
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
    Value(Arg),
    Add(Arg, Arg),
    Sub(Arg, Arg),
    Mult(Arg, Arg),
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

#[derive(Debug)]
enum ParseOperationError {
    UnknownOperator { operator: String },
    CannotParseArg { cause: ParseArgError },
}

impl Display for ParseOperationError {
    fn fmt(&self, f: &mut Formatter) -> std::fmt::Result {
        f.write_str("cannot parse operation: ");
        match self {
            ParseOperationError::UnknownOperator { operator } => {
                write!(f, "unknown operator {}", operator)
            }
            ParseOperationError::CannotParseArg { cause } => {
                write!(f, "cannot parse arg: {}", cause)
            }
        }
    }
}

impl Error for ParseOperationError {
    fn source(&self) -> Option<&(dyn Error + 'static)> {
        match self {
            ParseOperationError::CannotParseArg { cause } => Option::Some(cause),
            ParseOperationError::UnknownOperator { .. } => Option::None,
        }
    }
}

impl From<ParseArgError> for ParseOperationError {
    fn from(cause: ParseArgError) -> Self {
        ParseOperationError::CannotParseArg { cause }
    }
}

impl std::str::FromStr for Operation {
    type Err = ParseOperationError;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let splitted: Vec<&str> = s.split(" ").collect();
        let read = |i: usize| splitted[i].parse::<Arg>(); //TODO handle error

        match splitted[0] {
            "VALUE" => Ok(Operation::Value(read(1)?)),
            "ADD" => Ok(Operation::Add(read(1)?, read(2)?)),
            "SUB" => Ok(Operation::Sub(read(1)?, read(2)?)),
            "MULT" => Ok(Operation::Mult(read(1)?, read(2)?)),
            _ => Err(ParseOperationError::UnknownOperator {
                operator: String::from(splitted[0]),
            }),
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
