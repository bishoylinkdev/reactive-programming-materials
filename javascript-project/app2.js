const fs = require('fs');
fs.readFile('test-file.txt', (error, data) => {
    if (error)
        throw error;
    console.log(data.toString());
});

process();

function process() {
    console.log('process method');
}