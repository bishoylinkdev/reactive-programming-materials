const fs = require('fs');
const data = fs.readFileSync('test-file.txt');
console.log(data.toString());
process();

function process() {
    console.log('process method');
}