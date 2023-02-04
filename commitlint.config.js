module.exports = {
    parserPreset: {
        parserOpts: {
            headerPattern: /^(\[[A-Z]+-[0-9]\d*])?\s?(\w+)(\(.*\))?:\s(.*)$/,
            headerCorrespondence: ['issue', 'type', 'scope', 'subject']
        }
    },
    rules: {
        'header-max-length': [2, 'always', 72],
        'issue-empty': [1, 'never'],
        'type-empty': [2, 'never'],
        'type-enum': [
            2,
            'always',
            [
                'build',
                'chore',
                'docs',
                'feat',
                'fix',
                'refactor',
                'revert',
                'style',
                'test'
            ]
        ],
        'subject-empty': [2, 'never'],
        'subject-full-stop': [2, 'never', '.'],
        'body-leading-blank': [2, 'always']
    },
    plugins:
        [
            {
                rules: {
                    'issue-empty': ({issue, header}) => {
                        return [
                            issue !== null && issue.length !== 0,
                            'issue may not be empty'
                        ]
                    }
                }
            },
        ],
}