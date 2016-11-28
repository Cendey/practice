/**
 * <p>Project: MIT Liberal Project</p>
 * <p>Description: practice</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: MIT Liberal Co., Ltd.</p>
 *
 * @author <cendey@126.com>
 * @since 8/25/2016
 * @version 1.0
 */
"use strict";
let utils = require("./utils");

let expert = new utils.Person("Nicholas");
console.log(expert.getName());

let node = {
    type: "Identifier",
    name: "foo",
    loc: {
        start: {
            pos: 1,
            guard: 3
        },
        end: {
            row: 2,
            column: 4
        }
    },
    range: [5, 11, {start: 6, end: 10}],
    *[Symbol.iterator](){
        function* derived(item) {
            if (typeof item === "object") {
                for (const key of Reflect.ownKeys(item)) {
                    if (typeof item[key] === "object") {
                        yield* derived(item[key]);
                    } else {
                        yield item[key];
                    }
                }
            } else {
                yield item;
            }
        }

        for (const prop of Reflect.ownKeys(this)) {
            yield* derived(this[prop]);
        }
    }
};

let nodes = [...node];
console.log(nodes);
console.log(node[Symbol.iterator]);

for (let value of node) {
    console.log(value);
}

function * generatorFunction() {
    yield (yield 1)(yield 2)(yield 3)();
}

function third(num) {
    console.log(num);
    return 4;
}

function second(num) {
    console.log(num);
    return third;
}

function first(num) {
    console.log(num);
    return second;
}

let test = (first) => {
    console.log(first);
    return (second) => {
        console.log(second);
        return (third) => {
            console.log(third);
            return 4;
        };
    };
};

let walker = generatorFunction();
console.log(walker.next()); // {value: 1, done: false}
console.log(walker.next(first)); // {value: 2, done: false}
console.log(walker.next("a")); // "a" {value: 3, done: false}
console.log(walker.next("b")); // "b" undefined {value: 4, done: false}
console.log(walker.next("d")); // {value: undefined, done: true}

function *foo() {
    let arr = [yield 1, yield 2, yield 3];
    console.log([...arr, yield 4]);
}

let it = foo();
console.log(it.next());
console.log(it.next(5));
console.log(it.next(6));
console.log(it.next(7));
console.log(it.next(8));
console.log(it.next(9));


utils.defineCustomizedNumIterator();
for (let elem of 3) {
    console.log(elem);
}

console.log(...-3);

function *delegateIterator(begin, end) {
    for (let init = begin; init < end; init++) {
        yield *init;
    }
}

let numIterator = delegateIterator(1, 10);
for (let result of numIterator) {
    console.log(result);
}

function *fooGenerator(start) {
    if (start < 3) {
        start = yield *fooGenerator(start + 1);
    }
    return start * 2;
}
let fooIt = fooGenerator(1);
console.log(fooIt.next());

process.exit(0);