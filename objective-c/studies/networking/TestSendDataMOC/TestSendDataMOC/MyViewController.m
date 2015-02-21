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

@end

@implementation MyViewController

-(void)viewDidLoad {
    [super viewDidLoad];
    
    _buttonLeft = [[UIBarButtonItem alloc] initWithTitle:@"Left" style:UIBarButtonItemStylePlain target:self action:@selector(actionLeft:)];
    _buttonRight = [[UIBarButtonItem alloc] initWithTitle:@"Right" style:UIBarButtonItemStylePlain target:self action:@selector(actionRight:)];
    [self.navigationItem setLeftBarButtonItem:_buttonLeft];
    [self.navigationItem setRightBarButtonItem:_buttonRight];
    [self.navigationItem setTitle:@"Title"];
}

-(void)actionLeft:(id)sender {
    NSLog(@"[MyViewController actionLeft]");
    
}

-(void)actionRight:(id)sender {
    NSLog(@"[MyViewController actionRight]");
    
}

@end
