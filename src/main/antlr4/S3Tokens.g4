grammar S3Tokens;

value returns [String val]
	: SimpleValue {$val = $SimpleValue.text;}
	| DateValue {
		$val = $DateValue.text;
		$val = $val.substring(1, $val.length()-1);
	}
	| QuotedValue
		{
			$val = $QuotedValue.text;
			$val = $val.substring(1, $val.length()-1);
			$val = $val.replace("\\\"", "\""); // unescape the quotes
		}
	| DoubleQuotedValue
		{
			$val = $DoubleQuotedValue.text;
			$val = $val.substring(2, $val.length()-2);
			$val = $val.replace("\\\"\\\"", "\"\""); // unescape the quotes
		}
	;

NoValue
	: '-'
	;

SimpleValue
	: ALLOWED_CHAR+
	;

DateValue
	: '[' (ALLOWED_CHAR | DELIM)* ']'
	;

QuotedValue
	: '"' (ESCAPED_QUOTE | DELIM | ALLOWED_CHAR)* '"'
	;

DoubleQuotedValue
	: '""' (ESCAPED_QUOTE | DELIM | ALLOWED_CHAR)* '""'
	;

LINEBREAK
	: '\r'? '\n'
	| '\r'
	;

DELIM
	: ' '
	;

ESCAPED_QUOTE
	: '\\' '"'
	;

ALLOWED_CHAR
	: ~(' ' | '\t' | '\r' | '\n' | '"')
	;