module.exports = {
    parserPreset: {
        parserOpts: {
            // Chris Beams style: [TICKET-123] Add feature or Add feature
            headerPattern: /^(\[[A-Z]+-[0-9]+\]\s+)?([A-Z][a-z]+(\s+[a-z]+)*.*?)(\s*\([^)]*\))?$/,
            headerCorrespondence: ['issue', 'subject', 'scope']
        }
    },
    rules: {
        // Chris Beams rule: 50 characters for subject line
        'header-max-length': [2, 'always', 50],
        'subject-empty': [2, 'never'],
        // Chris Beams rule: no period at end
        'subject-full-stop': [2, 'never', '.'],
        // Chris Beams rule: capitalize first letter
        'subject-case': [2, 'always', 'sentence-case'],
        // Chris Beams rule: separate subject and body with blank line
        'body-leading-blank': [1, 'always'],
        // Chris Beams rule: wrap body at 72 characters
        'body-max-line-length': [2, 'always', 72]
    },
    plugins: [
        {
            rules: {
                // Custom rule to validate Chris Beams imperative mood
                'subject-imperative': (parsed) => {
                    const { subject } = parsed;
                    if (!subject) return [true];

                    // Common imperative verbs for commits
                    const imperativeVerbs = [
                        'Add', 'Fix', 'Update', 'Remove', 'Delete', 'Create', 'Implement',
                        'Refactor', 'Optimize', 'Improve', 'Change', 'Move', 'Rename',
                        'Extract', 'Split', 'Merge', 'Replace', 'Revert', 'Bump',
                        'Configure', 'Setup', 'Install', 'Upgrade', 'Downgrade'
                    ];

                    const firstWord = subject.split(' ')[0];
                    const isImperative = imperativeVerbs.includes(firstWord);

                    return [
                        isImperative,
                        `Subject must start with imperative verb (e.g., Add, Fix, Update). Found: "${firstWord}"`
                    ];
                }
            }
        }
    ]
}