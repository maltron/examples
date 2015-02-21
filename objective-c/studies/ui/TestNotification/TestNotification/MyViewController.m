//
//  MyViewController.m
//  HelloWorld
//
//  Created by Mauricio Leal on 1/11/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "MyViewController.h"
#import "AutoLayout.h"

@interface MyViewController()
@property (nonatomic, strong) UIBarButtonItem *buttonLeft, *buttonRight;
@property (nonatomic, strong) UIAlertView *alertView;

@end

@implementation MyViewController

-(void)viewDidLoad {
    [super viewDidLoad];
    
    _buttonLeft = [[UIBarButtonItem alloc] initWithTitle:@"Left" style:UIBarButtonItemStylePlain target:self action:@selector(actionLeft:)];
    _buttonRight = [[UIBarButtonItem alloc] initWithTitle:@"Right" style:UIBarButtonItemStylePlain target:self action:@selector(actionRight:)];
    [self.navigationItem setLeftBarButtonItem:_buttonLeft];
    [self.navigationItem setRightBarButtonItem:_buttonRight];
    [self.navigationItem setTitle:@"Title"];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(buttonLeft:) name:@"LEFT" object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(buttonRight:) name:@"RIGHT" object:nil];
}

-(void)actionLeft:(id)sender {
    NSLog(@"[MyViewController actionLeft]");
    [[NSNotificationCenter defaultCenter] postNotificationName:@"LEFT" object:nil];
    
}

-(void)actionRight:(id)sender {
    NSLog(@"[MyViewController actionRight]");
    [[NSNotificationCenter defaultCenter] postNotificationName:@"RIGHT" object:nil];
    
}

-(void)buttonLeft:(id)sender {
    if(!_alertView) _alertView = [[UIAlertView alloc] initWithTitle:nil message:nil delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
    
    [_alertView setTitle:@"BUTTON LEFT"];
    [_alertView setMessage:@"Button Left was pressed"];
    [_alertView setAlertViewStyle:UIAlertViewStyleDefault];
    [_alertView show];
}

-(void)buttonRight:(id)sender {
    if(!_alertView) _alertView = [[UIAlertView alloc] initWithTitle:nil message:nil delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
    
    [_alertView setTitle:@"BUTTON RIGHT"];
    [_alertView setMessage:@"Button Right was pressed"];
    [_alertView setAlertViewStyle:UIAlertViewStyleDefault];
    [_alertView show];
}

@end
