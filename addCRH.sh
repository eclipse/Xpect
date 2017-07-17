#!/bin/bash

# -e  == exit immediately
# -x  == enable debug. (+x for disable)
set +e +x

#YEAR=2010

for YEAR in 2010 2011 2012 2013 2014 2015 2016 2017 2018
do
	#YEAR=`expr $YEAR + 1`
	echo "YEAR=$YEAR"

CRH="/*******************************************************************************
 * Copyright (c) $YEAR itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/"

	# single line version of the copyright header
	CRHSL=$(echo "$CRH" | tr -d '\n')

	find -E . -iregex '.*\.(java|xtend|xt|xtext|mwe2|Jenkins)' -type f -print0 | while IFS= read -r -d '' file; do

		re='^[0-9]+$'
		if ! [[ $var =~ $re ]] ; then
		   var=0
		fi

		cat "$file" | tr -d "\n" | grep -qF "$CRHSL"
		if [ "$?" == "0" ]; then
			var=`expr $var + 1`
			echo "$var : Removing copyright header of $file"
			#cat "$i" | tail +8 "$file" >> "$file"
			echo "$(tail -n +8 $file)" > "$file"
			#cat "$i" | pbcopy && printf "$CRH" > "$i" && pbpaste >> "$i"
		fi

	done

done

exit 0;




find -E . -iregex '.*\.(java|xtend|xt|xtext|mwe2|Jenkins)' -type f -print0 | while IFS= read -r -d '' file; do
	echo "$file"
    cat "$file" | pbcopy && printf "/**
 * Copyright (c) 2012-2017 TypeFox GmbH and itemis AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Moritz Eysholdt - Initial contribution and API
 */

" > "$file" && pbpaste >> "$file"
    #cat "$file" | pbcopy && printf "<!--\nCopyright (c) 2013-2017 TypeFox GmbH and itemis AG.\nAll rights reserved. This program and the accompanying materials\nare made available under the terms of the Eclipse Public License v1.0\nwhich accompanies this distribution, and is available at\nhttp://www.eclipse.org/legal/epl-v10.html\n\nContributors:\n  Moritz Eysholdt - Initial contribution and API\n -->\n\n" > "$file" && pbpaste >> "$file"
done

#find -E . -iregex '.*\.(java|xtend|xsemantics|oldxsem|xtext)' -type f -exec bash -c 'echo "$1" && cat "$1" | pbcopy && printf "/*\n * Copyright (c) 2013-2017 TypeFox GmbH and itemis AG.\n * All rights reserved. This program and the accompanying materials\n * are made available under the terms of the Eclipse Public License v1.0\n * which accompanies this distribution, and is available at\n * http://www.eclipse.org/legal/epl-v10.html\n *\n * Contributors:\n *   Moritz Eysholdt - Initial contribution and API\n */\n\n" > "$1" && pbpaste >> "$1"' -- {} \;

#find . -name 'README.txt' -type f -exec bash -c 'echo "$1" && cat "$1" | pbcopy && printf "Copyright (c) 2013-2017 TypeFox GmbH and itemis AG.\nAll rights reserved. This program and the accompanying materials\nare made available under the terms of the Eclipse Public License v1.0\nwhich accompanies this distribution, and is available at\nhttp://www.eclipse.org/legal/epl-v10.html\n\nContributors:\n  Moritz Eysholdt - Initial contribution and API\n\n\n" > "$1" && pbpaste >> "$1"' -- {} \;

find . -name '*.md' -type f -exec bash -c 'echo "$1" && cat "$1" | pbcopy && printf "
<!--
Copyright (c) 2013-2017 TypeFox GmbH and itemis AG.
All rights reserved. This program and the accompanying materials
are made available under the terms of the Eclipse Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/epl-v10.html

Contributors:
  Moritz Eysholdt - Initial contribution and API
-->

" > "$1" && pbpaste >> "$1"' -- {} \;


