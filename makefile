test: test-ts test-rs

test-ts:
	make -C ts test

test-rs:
	make -C rust test
