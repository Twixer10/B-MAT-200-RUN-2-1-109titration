##
## EPITECH PROJECT, 2020
## makefile exe
## File description:
## fct
##

PACKAGE = 109/src/re/fazan/titration

SRC = $(PACKAGE)/Main.java		\

CLASSDIR = class

METAINF = META-INF/MANIFEST.MF

JARNAME	= 109titration.jar 

EXENAME	= exec.sh

NAME = 109titration

all: buildjar $(NAME)

buildjar:
	mkdir $(CLASSDIR)
	javac -d $(CLASSDIR) $(SRC)
	jar cfm $(JARNAME) $(METAINF) -C $(CLASSDIR) re

$(NAME):
	cp $(EXENAME) $(NAME)
	chmod +x $(NAME)

clean:
	rm -rf $(CLASSDIR)
	rm -f $(JARNAME)

fclean: clean
	rm -f $(NAME)

re:	fclean all
