/**
 * <p>Project: MIT Liberal Project</p>
 * <p>Description: helper</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: MIT Liberal Co., Ltd.</p>
 *
 * @author <cendey@126.com>
 * @since 9/1/2016
 * @version 1.0
 */
module.exports = {
    containsErrors: function (response) {
        if (!response || !response.responseText)
            return false;
        let errorValue = response.responseText;
        return String(errorValue.failure) == "true"
            || Boolean(errorValue.failure);

    },
    trace: function (msg) {
        let traceMessage = msg;
        if (msg.responseText) {
            traceMessage = msg.responseText.errorMessage;
        }
        console.log("[" + new Date().toLocaleDateString()
            + "] " + traceMessage);
    },
    defineCustomizedNumIterator: function () {
        if (!Number.prototype[Symbol.iterator]) {
            Object.defineProperty(Number.prototype, Symbol.iterator, {
                configurable: true,
                enumerable: false,
                writable: true,
                value: function iterator() {
                    let i = 0, top = +this, inc = top < 0 ? -1 : 1;

                    return {
                        [Symbol.iterator](){
                            return this;
                        },

                        next(){
                            if (i != top) {
                                if (top > 0) {
                                    i = (top > i + inc) ? (i + inc) : top;
                                } else {
                                    i = (top > i + inc) ? top : (i + inc);
                                }
                                return {value: i, done: false};
                            } else {
                                return {value: undefined, done: true}
                            }
                        }
                    }
                }
            });
        }
    },
    Person: (function () {
        let _data = {}, _uniqueId = 0;

        function Person(name) {
            if (new.target !== undefined) {
                Object.defineProperty(this, "identifier", {value: _uniqueId++});
                _data[this.identifier] = {
                    name: name
                };
            } else {
                throw new Error("Please invoke Person with new keyword!")
            }
        }

        Person.prototype.getName = function () {
            if (new.target == undefined) {
                return _data[this.identifier].name;
            } else {
                throw new Error("Please invoke getName without new keyword!")
            }

        };

        return Person;
    }())
};