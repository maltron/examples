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
@property (nonatomic, strong) UIBarButtonItem *buttonStart, *buttonStop;
@property (nonatomic, strong) NSURLSessionDataTask *task;

@end

@implementation MyViewController

-(void)viewDidLoad {
    [super viewDidLoad];
    
    _buttonStart = [[UIBarButtonItem alloc] initWithTitle:@"Start" style:UIBarButtonItemStylePlain target:self action:@selector(actionStart:)];
    _buttonStop = [[UIBarButtonItem alloc] initWithTitle:@"Stop" style:UIBarButtonItemStylePlain target:self action:@selector(actionStop:)];
    [_buttonStop setEnabled:NO];
    [self.navigationItem setLeftBarButtonItem:_buttonStart];
    [self.navigationItem setRightBarButtonItem:_buttonStop];
    [self.navigationItem setTitle:@"NSURLSession in Background"];
}

-(void)actionStart:(id)sender {
    NSLog(@"[MyViewController actionStart]");
    [self.buttonStart setEnabled:NO]; [self.buttonStop setEnabled:YES];
    
    
}

-(void)actionStop:(id)sender {
    NSLog(@"[MyViewController actionStop]");
    [self.buttonStart setEnabled:YES]; [self.buttonStop setEnabled:NO];
    
    
}

@end
