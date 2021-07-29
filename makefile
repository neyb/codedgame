test: test-ts test-rs test-kt

test-ts:
	make -C ts test

test-rs:
	make -C rust test

test-kt:
	make -C kotlin test
